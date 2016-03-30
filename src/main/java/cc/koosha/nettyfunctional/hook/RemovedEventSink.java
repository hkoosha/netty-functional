package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedEventHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedEventSink<T> extends MatchedEventHandler<T> {

    public RemovedEventSink() {
    }

    public RemovedEventSink(@NonNull final Matcher matcher) {
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
        ctx.pipeline().remove(this);
    }

    protected abstract void event1(ChannelHandlerContext ctx, T event)
            throws Exception;

}
