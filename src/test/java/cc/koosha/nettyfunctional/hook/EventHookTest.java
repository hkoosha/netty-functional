package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.val;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;


public class EventHookTest {

    @Test
    public void testUnsupportedEvent() throws Exception {

        val h0 = new EventHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        val em = new EmbeddedChannel(h0);

        em.pipeline().fireUserEventTriggered("do not touch me");
    }

    @Test
    public void testEvent1() throws Exception {

        final int add = 99;
        final int init = 22;
        final AtomicInteger read = new AtomicInteger(-1);

        val h0 = new EventHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                event.addAndGet(add);
            }
        };

        val h1 = new EventHook<String>(String.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  String msg) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        val h2 = new EventHook<AtomicInteger>(AtomicInteger.class) {

            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                read.set(event.get());
            }
        };

        val em = new EmbeddedChannel(h0, h1, h2);

        em.pipeline().fireUserEventTriggered(new AtomicInteger(init));
        assertEquals(read.get(), init + add);
    }

}
