package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;


public abstract class RemovedOutboundSink<O> extends MatchedOutboundHandler<O> {

    protected RemovedOutboundSink() {
        super();
    }

    protected RemovedOutboundSink(final Class<?> clazz) {
        super(clazz);
    }

    protected RemovedOutboundSink(final Matcher matcher) {
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
        try {
            this.write1(ctx, msg, promise);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        ctx.pipeline().remove(this);
    }

    protected abstract void write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
