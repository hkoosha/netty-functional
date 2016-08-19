package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import lombok.NonNull;


public abstract class RemovedOutboundTransformer<O> extends MatchedOutboundHandler<O> {

    protected RemovedOutboundTransformer() {
    }

    protected RemovedOutboundTransformer(@NonNull final Class<?> clazz) {

        super(clazz);
    }

    protected RemovedOutboundTransformer(@NonNull final Matcher matcher) {

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
                                final O msg,
                                final ChannelPromise promise) throws Exception {

        Object result;

        try {
            result = this.write1(ctx, msg, promise);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if(result != null) {
            ctx.pipeline().remove(this);
            ctx.write(result, promise);
        }

    }

    protected abstract Object write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
