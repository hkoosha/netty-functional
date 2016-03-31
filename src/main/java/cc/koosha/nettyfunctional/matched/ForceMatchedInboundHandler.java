package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.NonNull;


public abstract class ForceMatchedInboundHandler<I> extends MatchedInboundHandler<I> {

    protected ForceMatchedInboundHandler() {

        super();
    }

    protected ForceMatchedInboundHandler(@NonNull final Class<?> clazz) {

        super(clazz);
    }

    protected ForceMatchedInboundHandler(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(@NonNull final ChannelHandlerContext ctx, final Object msg) {

        final String type = msg == null ? "null" : msg.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
