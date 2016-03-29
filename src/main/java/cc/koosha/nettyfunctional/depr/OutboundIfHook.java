package cc.koosha.nettyfunctional.depr;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.hook.OutboundHook;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


@Deprecated
public abstract class OutboundIfHook<T> extends OutboundHook<T> {

    public OutboundIfHook() {
    }

    public OutboundIfHook(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void write1(final ChannelHandlerContext ctx,
                                final T msg,
                                final ChannelPromise promise) throws Exception {

        final boolean callNext = this.write2(ctx, msg, promise);

        if(callNext)
            ctx.write(msg, promise);
    }

    protected abstract boolean write2(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;


}
