package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedBidiHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;


public abstract class BidiTransform<I, O> extends MatchedBidiHandler<I, O> {

    protected BidiTransform() {
        super();
    }

    protected BidiTransform(final Class<?> iType,
                            final Class<?> oType) {
        super(iType, oType);
    }

    protected BidiTransform(final Matcher iMatcher,
                            final Matcher oMatcher) {
        super(iMatcher, oMatcher);
    }


    @Override
    protected final void iUnsupportedMsg(final ChannelHandlerContext ctx,
                                         final Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    protected final void read0(final ChannelHandlerContext ctx,
                               final I msg) throws Exception {
        Object result;

        try {
            result = this.read1(ctx, msg);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if (result != null)
            ctx.fireChannelRead(result);
    }


    @Override
    protected final void oUnsupportedMsg(final ChannelHandlerContext ctx,
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

        if (result != null)
            ctx.write(result, promise);
    }


    protected abstract Object read1(final ChannelHandlerContext ctx, final I msg)
            throws Exception;

    protected abstract Object write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;
}
