package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class RemovedOutboundHook<T> extends OutboundHook<T> {

    public RemovedOutboundHook() {
    }

    public RemovedOutboundHook(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void write1(final ChannelHandlerContext ctx,
                                final T msg,
                                final ChannelPromise promise) throws Exception {

        this.write2(ctx, msg, promise);
        ctx.write(msg, promise);
        ctx.pipeline().remove(this);
    }

    protected abstract void write2(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;

}
