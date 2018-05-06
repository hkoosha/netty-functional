package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class OutboundHookTest {

    @Test
    public void testUnsupportedMsg() throws Exception {

        OutboundHook<AtomicInteger> h0 = new OutboundHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void write1(ChannelHandlerContext ctx,
                                  AtomicInteger msg,
                                  ChannelPromise promise) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0);

        assert em.writeOutbound("do not touch me");
        final String read = em.readOutbound();
        assertEquals(read, "do not touch me");
    }

    @Test
    public void testWrite1() throws Exception {

        final int add = 99;
        final int init = 22;

        OutboundHook<AtomicInteger> h0 = new OutboundHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void write1(ChannelHandlerContext ctx,
                                  AtomicInteger msg,
                                  ChannelPromise promise) throws Exception {

                msg.addAndGet(add);
            }
        };

        OutboundHook<String> h1 = new OutboundHook<String>(String.class) {
            @Override
            protected void write1(ChannelHandlerContext ctx, String msg, ChannelPromise promise) throws Exception {
                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0, h1);

        assert em.writeOutbound(new AtomicInteger(init));
        final AtomicInteger read = em.readOutbound();

        assertEquals(read.get(), init + add);
    }
}
