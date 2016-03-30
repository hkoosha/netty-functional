package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.matched.MatchedInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class InboundTransformer<T> extends MatchedInboundHandler<T> {

    public InboundTransformer() {
    }

    public InboundTransformer(@NonNull final Matcher matcher) {

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

        if(result != null)
            ctx.fireChannelRead(result);
    }

    abstract protected Object read1(final ChannelHandlerContext ctx, final T msg)
            throws Exception;

}
