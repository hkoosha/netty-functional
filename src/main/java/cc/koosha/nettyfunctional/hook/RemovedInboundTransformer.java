package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;
import lombok.val;


public abstract class RemovedInboundTransformer<T> extends InboundHook<T> {

    public RemovedInboundTransformer() {
    }

    public RemovedInboundTransformer(@NonNull Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void read1(final ChannelHandlerContext ctx,
                               final T msg) throws Exception {

        final Object result = this.read2(ctx, msg);

        if(result != null) {
            ctx.fireChannelRead(msg);
            ctx.pipeline().remove(this);
        }
    }

    protected abstract Object read2(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
