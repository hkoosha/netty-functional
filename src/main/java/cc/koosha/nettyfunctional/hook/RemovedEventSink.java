package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedEventHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class RemovedEventSink<E> extends MatchedEventHandler<E> {

    protected RemovedEventSink() {
        super();
    }

    protected RemovedEventSink(final Class<?> type) {
        super(type);
    }

    protected RemovedEventSink(final Matcher matcher) {
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
        this.event1(ctx, event);
        ctx.pipeline().remove(this);
    }

    protected abstract void event1(ChannelHandlerContext ctx, E event)
            throws Exception;

}
