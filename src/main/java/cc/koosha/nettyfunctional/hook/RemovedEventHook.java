package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedEventHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;


@ChannelHandler.Sharable
public abstract class RemovedEventHook<E> extends MatchedEventHandler<E> {

    protected RemovedEventHook() {
        super();
    }

    protected RemovedEventHook(final Class<?> type) {
        super(type);
    }

    protected RemovedEventHook(final Matcher matcher) {
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
        ctx.fireUserEventTriggered(event);
        ctx.pipeline().remove(this);
    }

    protected abstract void event1(ChannelHandlerContext ctx, E event)
            throws Exception;

}
