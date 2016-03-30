package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class RemovedOutboundTransformer<T> extends MatchedOutboundHandler<T> {

    public RemovedOutboundTransformer() {
    }

    public RemovedOutboundTransformer(@NonNull final Matcher matcher) {

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

        final Object result = this.write1(ctx, msg, promise);

        if(result != null) {
            ctx.write(result, promise);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object write1(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;

}
