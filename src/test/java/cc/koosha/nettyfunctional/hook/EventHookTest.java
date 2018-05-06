package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;


public class EventHookTest {

    @Test
    public void testUnsupportedEvent() throws Exception {

        EventHook<AtomicInteger> h0 = new EventHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0);

        em.pipeline().fireUserEventTriggered("do not touch me");
    }

    @Test
    public void testEvent1() throws Exception {

        final int add = 99;
        final int init = 22;
        final AtomicInteger read = new AtomicInteger(-1);

        EventHook<AtomicInteger> h0 = new EventHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                event.addAndGet(add);
            }
        };

        EventHook<String> h1 = new EventHook<String>(String.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  String msg) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EventHook<AtomicInteger> h2 = new EventHook<AtomicInteger>(AtomicInteger.class) {

            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                read.set(event.get());
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0, h1, h2);

        em.pipeline().fireUserEventTriggered(new AtomicInteger(init));
        assertEquals(read.get(), init + add);
    }

}
