package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedEventHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


@ChannelHandler.Sharable
public abstract class RemovedIfEventSink<T> extends MatchedEventHandler<T> {

    protected RemovedIfEventSink() {
    }

    protected RemovedIfEventSink(@NonNull final Class<?> type) {

        super(type);
    }

    protected RemovedIfEventSink(@NonNull final Matcher matcher) {

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

        boolean result = this.event1(ctx, event);

        if(result)
            ctx.pipeline().remove(this);
    }

    protected abstract boolean event1(ChannelHandlerContext ctx, T event)
            throws Exception;

}
