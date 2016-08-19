package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class EventSink<E> extends EventTransformer<E> {

    public EventSink() {
    }

    public EventSink(@NonNull final Class<?> type) {
        super(type);
    }

    public EventSink(@NonNull final Matcher matcher) {
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

