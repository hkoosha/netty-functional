package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class RemovedOutboundTransformer<T> extends OutboundHook<T> {

    public RemovedOutboundTransformer() {
    }

    public RemovedOutboundTransformer(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void write1(final ChannelHandlerContext ctx,
                                final T msg,
                                final ChannelPromise promise) throws Exception {

        final Object result = this.write2(ctx, msg, promise);

        if(result != null) {
            ctx.fireChannelRead(result);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object write2(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;

}
