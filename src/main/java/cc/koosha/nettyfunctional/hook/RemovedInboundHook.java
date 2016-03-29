package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedInboundHook<T> extends InboundHook<T> {

    public RemovedInboundHook() {
    }

    public RemovedInboundHook(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected void read1(final ChannelHandlerContext ctx,
                         final T msg) throws Exception {

        this.read2(ctx, msg);
        ctx.fireChannelRead(msg);
        ctx.pipeline().remove(this);
    }

    protected abstract void read2(ChannelHandlerContext ctx, T msg)
            throws Exception;

}
