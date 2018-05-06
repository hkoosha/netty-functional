package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class OutboundHook<O> extends MatchedOutboundHandler<O> {

    protected OutboundHook() {
        super();
    }

    protected OutboundHook(@NonNull final Class<?> clazz) {
        super(clazz);
    }

    protected OutboundHook(@NonNull final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected void unsupportedMsg(@NonNull final ChannelHandlerContext ctx,
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
