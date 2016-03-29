package cc.koosha.nettyfunctional.depr;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.hook.InboundHook;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


@Deprecated
public abstract class InboundIfHook<T> extends InboundHook<T> {

    public InboundIfHook() {
    }

    public InboundIfHook(@NonNull final Matcher matcher) {

        super(matcher);
    }

    @Override
    protected final void read1(final ChannelHandlerContext ctx,
                               final T msg) throws Exception {

        final boolean callNext = this.channelRead2(ctx, msg);

        if(callNext)
            ctx.fireChannelRead(msg);
    }

    protected abstract boolean channelRead2(ChannelHandlerContext ctx, T msg)
            throws Exception;

}

