package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


public abstract class RemovedOutboundHook<O> extends MatchedOutboundHandler<O> {

    protected RemovedOutboundHook() {
        super();
    }

    protected RemovedOutboundHook(final Class<?> clazz) {
        super(clazz);
    }

    protected RemovedOutboundHook(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    protected final void write0(final ChannelHandlerContext ctx,
                                final O msg,
                                final ChannelPromise promise) throws Exception {
        this.write1(ctx, msg, promise);
        ctx.pipeline().remove(this);
        ctx.write(msg, promise);
    }

    protected abstract void write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
