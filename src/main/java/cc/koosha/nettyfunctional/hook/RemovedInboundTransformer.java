package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.NonNull;


public abstract class RemovedInboundTransformer<T> extends MatchedInboundHandler<T> {

    protected RemovedInboundTransformer() {
    }

    protected RemovedInboundTransformer(@NonNull final Class<?> clazz) {

        super(clazz);
    }

    protected RemovedInboundTransformer(@NonNull final Matcher matcher) {

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

        Object result;

        try {
            result = this.read1(ctx, msg);
        }
        finally {
            ReferenceCountUtil.release(msg);
        }

        if(result != null) {
            ctx.fireChannelRead(result);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object read1(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
