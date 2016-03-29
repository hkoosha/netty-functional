package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class RemovedInboundSink<T> extends InboundHook<T> {

    public RemovedInboundSink() {
    }

    public RemovedInboundSink(@NonNull final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final void read1(final ChannelHandlerContext ctx,
                               final T read) throws Exception {

        this.read2(ctx, read);
        ctx.pipeline().remove(this);
    }

    protected abstract void read2(ChannelHandlerContext ctx, T read)
            throws Exception;

}
