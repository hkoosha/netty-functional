package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;


public abstract class ForceMatchedEventHandler<E> extends MatchedEventHandler<E> {

    protected ForceMatchedEventHandler() {
    }

    protected ForceMatchedEventHandler(final Class<?> type) {
        super(type);
    }

    protected ForceMatchedEventHandler(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedEvent(final ChannelHandlerContext ctx,
                                          final Object evt) {
        final String type = evt == null ? "null" : evt.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
