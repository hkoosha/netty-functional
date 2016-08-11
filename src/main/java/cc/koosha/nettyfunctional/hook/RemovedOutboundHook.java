package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class RemovedOutboundHook<T> extends MatchedOutboundHandler<T> {

    protected RemovedOutboundHook() {
    }

    protected RemovedOutboundHook(@NonNull final Class<?> clazz) {

        super(clazz);
    }

    protected RemovedOutboundHook(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {

        // skip to next handler
        ctx.write(msg, promise);
    }

    @Override
    protected final void write0(final ChannelHandlerContext ctx,
                                final T msg,
                                final ChannelPromise promise) throws Exception {

        this.write1(ctx, msg, promise);
        ctx.pipeline().remove(this);
        ctx.write(msg, promise);
    }

    protected abstract void write1(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;

}
