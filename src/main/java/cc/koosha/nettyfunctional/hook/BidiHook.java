package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedBidiHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


@SuppressWarnings("unused")
public abstract class BidiHook<I, O> extends MatchedBidiHandler<I, O> {

    protected BidiHook() {
        super();
    }

    protected BidiHook(final Class<?> iType,
                       final Class<?> oType) {
        super(iType, oType);
    }

    protected BidiHook(final Matcher iMatcher,
                       final Matcher oMatcher) {
        super(iMatcher, oMatcher);
    }

    @Override
    protected void iUnsupportedMsg(final ChannelHandlerContext ctx, final Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    protected void oUnsupportedMsg(final ChannelHandlerContext ctx,
                                   final Object msg,
                                   final ChannelPromise promise) {
        ctx.write(msg, promise);
    }


    @Override
    protected final void read0(final ChannelHandlerContext ctx,
                               final I msg) throws Exception {
        this.read1(ctx, msg);
        ctx.fireChannelRead(msg);
    }

    protected abstract void read1(ChannelHandlerContext ctx, I msg)
            throws Exception;


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
