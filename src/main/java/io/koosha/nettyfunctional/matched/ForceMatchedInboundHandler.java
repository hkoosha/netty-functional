package io.koosha.nettyfunctional.matched;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;


public abstract class ForceMatchedInboundHandler<I> extends MatchedInboundHandler<I> {

    protected ForceMatchedInboundHandler() {
        super();
    }

    protected ForceMatchedInboundHandler(final Class<?> clazz) {
        super(clazz);
    }

    protected ForceMatchedInboundHandler(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg) {
        final String type = msg == null ? "null" : msg.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
