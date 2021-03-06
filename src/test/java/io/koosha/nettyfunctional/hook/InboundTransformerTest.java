package io.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class InboundTransformerTest {

    @Test
    public void testRead1() throws Exception {

        InboundTransformer<String> h0 = new InboundTransformer<String>(String.class) {
            @Override
            protected Object read1(ChannelHandlerContext ctx,
                                   String msg) throws Exception {

                return Integer.parseInt(msg);
            }
        };

        InboundTransformer<Boolean> h1 = new InboundTransformer<Boolean>(Boolean.class) {
            @Override
            protected Object read1(ChannelHandlerContext ctx, Boolean msg) throws Exception {
                throw new UnsupportedOperationException("should not reach here");
            }
        };

        EmbeddedChannel em = new EmbeddedChannel(h0, h1);

        assert em.writeInbound("99");
        final Integer read = em.readInbound();
        assertEquals(read.intValue(), 99);
    }

}
