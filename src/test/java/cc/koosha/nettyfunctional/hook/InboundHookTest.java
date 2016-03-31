package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.val;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;


public class InboundHookTest {

    @Test
    public void testUnsupportedMsg() throws Exception {

        val h0 = new InboundHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void read1(ChannelHandlerContext ctx,
                                 AtomicInteger event) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        val em = new EmbeddedChannel(h0);

        assert em.writeInbound("do not touch me");
        final String read = em.readInbound();
        assertEquals(read, "do not touch me");
    }

    @Test
    public void testRead1() throws Exception {

        final int add = 99;
        final int init = 22;

        val h0 = new InboundHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void read1(ChannelHandlerContext ctx, AtomicInteger event) throws Exception {
                event.addAndGet(add);
            }
        };

        val h1 = new InboundHook<String>(String.class) {
            @Override
            protected void read1(ChannelHandlerContext ctx, String msg) throws Exception {
                throw new UnsupportedOperationException("should not reach here");
            }
        };

        val em = new EmbeddedChannel(h0, h1);

        assert em.writeInbound(new AtomicInteger(init));
        final AtomicInteger read = em.readInbound();

        assertEquals(read.get(), init + add);
    }

}
