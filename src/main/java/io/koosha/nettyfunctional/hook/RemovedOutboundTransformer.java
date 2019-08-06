package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;


public abstract class RemovedOutboundTransformer<O> extends MatchedOutboundHandler<O> {

    protected RemovedOutboundTransformer() {
        super();
    }

    protected RemovedOutboundTransformer(final Class<?> clazz) {
        super(clazz);
    }

    protected RemovedOutboundTransformer(final Matcher matcher) {
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
        Object result;

        try {
            result = this.write1(ctx, msg, promise);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if (result != null) {
            ctx.pipeline().remove(this);
            ctx.write(result, promise);
        }

    }

    protected abstract Object write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
