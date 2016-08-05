package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedOutboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import lombok.NonNull;


public abstract class RemovedOutboundSink<T> extends MatchedOutboundHandler<T> {

    protected RemovedOutboundSink() {
    }

    protected RemovedOutboundSink(@NonNull final Class<?> clazz) {

        super(clazz);
    }

    protected RemovedOutboundSink(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected final void unsupportedMsg(final ChannelHandlerContext ctx,
                                        final Object msg,
                                        final ChannelPromise promise) {

        // skip to next handler
        ctx.write(msg, promise);
    }

    @Override
    protected final void write0(final ChannelHandlerContext ctx,
                                final T msg,
                                final ChannelPromise promise) throws Exception {

        try {
            this.write1(ctx, msg, promise);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        ctx.pipeline().remove(this);
    }

    protected abstract void write1(ChannelHandlerContext ctx, T msg, ChannelPromise promise)
            throws Exception;

}
