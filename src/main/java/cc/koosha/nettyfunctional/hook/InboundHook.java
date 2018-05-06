package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class InboundHook<T> extends MatchedInboundHandler<T> {

    protected InboundHook() {
        super();
    }

    protected InboundHook(@NonNull final Class<?> clazz) {
        super(clazz);
    }

    protected InboundHook(@NonNull final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    protected final void read0(final ChannelHandlerContext ctx,
                               final T msg) throws Exception {
        this.read1(ctx, msg);
        ctx.fireChannelRead(msg);
    }

    protected abstract void read1(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
