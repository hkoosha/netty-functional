package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedEventSink<T> extends EventHook<T> {

    public RemovedEventSink() {
    }

    public RemovedEventSink(@NonNull final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final void event1(final ChannelHandlerContext ctx,
                                final T event) throws Exception {

        this.event2(ctx, event);
        ctx.pipeline().remove(this);
    }

    protected abstract void event2(ChannelHandlerContext ctx, T event)
            throws Exception;

}
