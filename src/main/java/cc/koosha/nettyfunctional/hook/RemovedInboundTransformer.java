package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedInboundTransformer<T> extends MatchedInboundHandler<T> {

    public RemovedInboundTransformer() {
    }

    public RemovedInboundTransformer(@NonNull Matcher matcher) {

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

        final Object result = this.read1(ctx, msg);

        if(result != null) {
            ctx.fireChannelRead(msg);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object read1(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
