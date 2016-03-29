package cc.koosha.nettyfunctional.matched;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.NonNull;


public abstract class ForceMatchedOutboundHandler<T> extends MatchedOutboundHandler<T> {

    @Override
    protected final void unsupportedMsg(@NonNull final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {

        final String type = msg == null ? "null" : msg.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
