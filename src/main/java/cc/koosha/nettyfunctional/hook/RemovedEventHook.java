package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedEventHook<T> extends EventHook<T> {

    public RemovedEventHook() {
    }

    public RemovedEventHook(@NonNull final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final void event1(final ChannelHandlerContext ctx,
                                final T event) throws Exception {

        this.event2(ctx, event);
        ctx.fireUserEventTriggered(event);
        ctx.pipeline().remove(this);
    }

    protected abstract void event2(ChannelHandlerContext ctx, T event)
            throws Exception;

}
