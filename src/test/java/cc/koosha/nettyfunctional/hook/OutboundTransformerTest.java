package cc.koosha.nettyfunctional.hook;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.val;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class OutboundTransformerTest {

    @Test
    public void testWrite1() throws Exception {

        val h0 = new OutboundTransformer<String>(String.class) {
            @Override
            protected Object write1(ChannelHandlerContext ctx, String msg, ChannelPromise promise) throws Exception {
                return Integer.parseInt(msg);
            }
        };

        val h1 = new OutboundTransformer<Boolean>(Boolean.class) {
            @Override
            protected Object write1(ChannelHandlerContext ctx, Boolean msg, ChannelPromise promise) throws Exception {
                throw new UnsupportedOperationException("should not reach here");
            }
        };

        val em = new EmbeddedChannel(h0, h1);

        assert em.writeOutbound("99");
        final Integer read = em.readOutbound();
        assertEquals(read.intValue(), 99);
    }
}
