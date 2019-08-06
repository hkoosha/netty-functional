package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


public abstract class OutboundHook<O> extends MatchedOutboundHandler<O> {

    protected OutboundHook() {
        super();
    }

    protected OutboundHook(final Class<?> clazz) {
        super(clazz);
    }

    protected OutboundHook(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected void unsupportedMsg(final ChannelHandlerContext ctx,
                                  final Object msg,
                                  final ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    protected final void write0(final ChannelHandlerContext ctx,
                                final O msg,
                                final ChannelPromise promise) throws Exception {
        this.write1(ctx, msg, promise);
        ctx.write(msg, promise);
    }

    protected abstract void write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
