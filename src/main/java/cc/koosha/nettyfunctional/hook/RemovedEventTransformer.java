package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedEventTransformer<T> extends EventHook<T> {

    public RemovedEventTransformer() {
    }

    public RemovedEventTransformer(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void event1(final ChannelHandlerContext ctx,
                                final T event) throws Exception {

        final Object result = this.event2(ctx, event);

        if(result != null) {
            ctx.fireUserEventTriggered(result);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object event2(ChannelHandlerContext ctx, T event)
            throws Exception;

}
