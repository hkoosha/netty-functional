package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;


public class RemovedEventHookTest {

    @Test
    public void testEvent1() throws Exception {

        final int add = 99;
        final int init = 22;
        final AtomicInteger read = new AtomicInteger(-1);

        RemovedEventHook<AtomicInteger> h0 = new RemovedEventHook<AtomicInteger>(AtomicInteger.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                event.addAndGet(add);
            }
        };

        RemovedEventHook<String> h1 = new RemovedEventHook<String>(String.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  String msg) throws Exception {

                throw new UnsupportedOperationException("should not reach here");
            }
        };

        RemovedEventHook<AtomicInteger> h2 = new RemovedEventHook<AtomicInteger>(AtomicInteger.class) {

            @Override
            protected void event1(ChannelHandlerContext ctx,
                                  AtomicInteger event) throws Exception {

                read.set(event.get());
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0, h1, h2);

        final int present = em.pipeline().names().size();
        em.pipeline().fireUserEventTriggered(new AtomicInteger(init));
        assertEquals(read.get(), init + add);
        final int removed = em.pipeline().names().size();
        assertEquals(removed, present - 2);
    }

}
