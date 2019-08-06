package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class EventSink<E> extends EventTransformer<E> {

    public EventSink() {
        super();
    }

    public EventSink(final Class<?> type) {
        super(type);
    }

    public EventSink(final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Object event1(final ChannelHandlerContext ctx,
                                  final E event) throws Exception {
        this.event2(ctx, event);
        return null;
    }

    protected abstract void event2(final ChannelHandlerContext ctx, final E event) throws Exception;

}

