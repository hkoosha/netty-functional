package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;


public abstract class RemovedIfOutboundSink<O> extends MatchedOutboundHandler<O> {

    protected RemovedIfOutboundSink() {
        super();
    }

    protected RemovedIfOutboundSink(final Class<?> type) {
        super(type);
    }

    protected RemovedIfOutboundSink(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {
        ctx.write(msg, promise);
    }

    @Override
    protected final void write0(final ChannelHandlerContext ctx,
                                final O msg,
                                final ChannelPromise promise) throws Exception {
        final boolean result;

        try {
            result = this.write1(ctx, msg, promise);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if (result)
            ctx.pipeline().remove(this);
    }

    protected abstract boolean write1(ChannelHandlerContext ctx, O msg, ChannelPromise promise)
            throws Exception;

}
