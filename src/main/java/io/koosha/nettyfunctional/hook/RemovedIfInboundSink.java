package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedInboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;


public abstract class RemovedIfInboundSink<I> extends MatchedInboundHandler<I> {

    protected RemovedIfInboundSink() {
        super();
    }

    protected RemovedIfInboundSink(final Class<?> type) {
        super(type);
    }

    protected RemovedIfInboundSink(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    protected final void read0(final ChannelHandlerContext ctx,
                               final I msg) throws Exception {
        final boolean result;

        try {
            result = this.read1(ctx, msg);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if (result)
            ctx.pipeline().remove(this);
    }

    protected abstract boolean read1(ChannelHandlerContext ctx, I msg)
            throws Exception;

}
