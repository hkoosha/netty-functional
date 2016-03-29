package cc.koosha.nettyfunctional.depr;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.hook.EventHook;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


@Deprecated
public abstract class EventIfHook<T> extends EventHook<T> {

    public EventIfHook() {
    }

    public EventIfHook(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void event1(final ChannelHandlerContext ctx,
                               final T event) throws Exception {

        final boolean callNext = this.event2(ctx, event);

        if(callNext)
            ctx.fireUserEventTriggered(event);
    }

    protected abstract boolean event2(ChannelHandlerContext ctx, T event)
            throws Exception;

}

