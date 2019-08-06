package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedEventHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class RemovedIfEventSink<E> extends MatchedEventHandler<E> {

    protected RemovedIfEventSink() {
        super();
    }

    protected RemovedIfEventSink(final Class<?> type) {
        super(type);
    }

    protected RemovedIfEventSink(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedEvent(final ChannelHandlerContext ctx,
                                          final Object event) {
        ctx.fireUserEventTriggered(event);
    }

    @Override
    protected final void event0(final ChannelHandlerContext ctx,
                                final E event) throws Exception {
        boolean result = this.event1(ctx, event);
        if (result)
            ctx.pipeline().remove(this);
    }

    protected abstract boolean event1(ChannelHandlerContext ctx, E event)
            throws Exception;

}
