package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedEventHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class EventTransformer<E> extends MatchedEventHandler<E> {

    protected EventTransformer() {
        super();
    }

    protected EventTransformer(final Class<?> type) {
        super(type);
    }

    protected EventTransformer(final Matcher matcher) {
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
        final Object result = this.event1(ctx, event);
        if(result != null)
            ctx.fireUserEventTriggered(result);
    }

    protected abstract Object event1(ChannelHandlerContext ctx, E event)
            throws Exception;

}
