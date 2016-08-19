package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.NonNull;


public abstract class ForceMatchedEventHandler<E> extends MatchedEventHandler<E> {

    protected ForceMatchedEventHandler() {
    }

    protected ForceMatchedEventHandler(@NonNull final Class<?> type) {

        super(type);
    }

    protected ForceMatchedEventHandler(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected final void unsupportedEvent(@NonNull final ChannelHandlerContext ctx,
                                          final Object evt) {

        final String type = evt == null ? "null" : evt.getClass().getName();
        throw new UnsupportedMessageTypeException(type);
    }

}
