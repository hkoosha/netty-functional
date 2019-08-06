package io.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;


public class EventTransformerTest {

    @Test
    public void testEvent1() throws Exception {

        final AtomicInteger read = new AtomicInteger(-1);

        EventTransformer<String> h0 = new EventTransformer<String>(String.class) {
            @Override
            protected Object event1(ChannelHandlerContext ctx, String event) throws Exception {
                return Integer.parseInt(event);
            }
        };

        EventTransformer<Boolean> h1 = new EventTransformer<Boolean>(Boolean.class) {
            @Override
            protected Object event1(ChannelHandlerContext ctx, Boolean event) throws Exception {
                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EventHook<Integer> catcher = new EventHook<Integer>(Integer.class) {
            @Override
            protected void event1(ChannelHandlerContext ctx, Integer event) throws Exception {
                read.set(event);
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0, h1, catcher);

        em.pipeline().fireUserEventTriggered("99");
        assertEquals(read.intValue(), 99);
    }
}
