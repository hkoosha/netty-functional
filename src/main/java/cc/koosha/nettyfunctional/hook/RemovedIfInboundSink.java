package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.NonNull;


@ChannelHandler.Sharable
public abstract class RemovedIfInboundSink<T> extends MatchedInboundHandler<T> {

    protected RemovedIfInboundSink() {

        super();
    }

    protected RemovedIfInboundSink(@NonNull final Class<?> type) {

        super(type);
    }

    protected RemovedIfInboundSink(@NonNull final Matcher matcher) {

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

        boolean result;

        try {
            result = this.read1(ctx, msg);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if(result)
            ctx.pipeline().remove(this);
    }

    protected abstract boolean read1(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
