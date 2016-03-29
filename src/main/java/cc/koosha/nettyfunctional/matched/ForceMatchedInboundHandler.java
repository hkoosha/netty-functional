package cc.koosha.nettyfunctional.matched;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.NonNull;


public abstract class ForceMatchedInboundHandler<I> extends MatchedInboundHandler<I> {

    @Override
    protected final void unsupportedMsg(@NonNull final ChannelHandlerContext ctx, final Object msg) {

        final String type = msg == null ? "null" : msg.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
