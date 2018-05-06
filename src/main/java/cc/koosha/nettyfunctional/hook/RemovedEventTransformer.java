package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedEventHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class RemovedEventTransformer<E> extends MatchedEventHandler<E> {

    protected RemovedEventTransformer() {
        super();
    }

    protected RemovedEventTransformer(final Class<?> type) {
        super(type);
    }

    protected RemovedEventTransformer(final Matcher matcher) {
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
        if (result != null) {
            ctx.fireUserEventTriggered(result);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object event1(ChannelHandlerContext ctx, E event)
            throws Exception;

}
