package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedInboundHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;


public abstract class RemovedInboundSink<I> extends MatchedInboundHandler<I> {

    protected RemovedInboundSink() {
        super();
    }

    protected RemovedInboundSink(final Class<?> type) {
        super(type);
    }

    protected RemovedInboundSink(final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    protected final void read0(final ChannelHandlerContext ctx,
                               final I read) throws Exception {
        try {
            this.read1(ctx, read);
        }
        finally {
            ReferenceCountUtil.release(read);
        }

        ctx.pipeline().remove(this);
    }

    protected abstract void read1(ChannelHandlerContext ctx, I read) throws Exception;

}
