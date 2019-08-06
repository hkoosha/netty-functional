package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedEventHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class EventHook<T> extends MatchedEventHandler<T> {

    protected EventHook() {
        super();
    }

    protected EventHook(final Class<?> type) {
        super(type);
    }

    protected EventHook(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedEvent(final ChannelHandlerContext ctx,
                                          final Object event) {
        ctx.fireUserEventTriggered(event);
    }

    @Override
    protected final void event0(final ChannelHandlerContext ctx,
                                final T event) throws Exception {
        this.event1(ctx, event);
        ctx.fireUserEventTriggered(event);
    }

    protected abstract void event1(ChannelHandlerContext ctx, T event)
            throws Exception;

}
