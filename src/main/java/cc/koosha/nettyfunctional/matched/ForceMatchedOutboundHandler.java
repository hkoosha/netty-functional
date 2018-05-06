package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.UnsupportedMessageTypeException;


public abstract class ForceMatchedOutboundHandler<O> extends MatchedOutboundHandler<O> {

    protected ForceMatchedOutboundHandler() {
        super();
    }

    protected ForceMatchedOutboundHandler(final Class<?> clazz) {
        super(clazz);
    }

    protected ForceMatchedOutboundHandler(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {
        final String type = msg == null ? "null" : msg.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
