package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class InboundHook<T> extends MatchedInboundHandler<T> {

    public InboundHook() {
    }

    public InboundHook(@NonNull Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg) {

        // skip message and call next handler.
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
