package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class InboundSink<I> extends InboundTransformer<I> {

    public InboundSink() {
        super();
    }

    public InboundSink(final Class<?> clazz) {
        super(clazz);
    }

    public InboundSink(final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Object read1(final ChannelHandlerContext ctx,
                                 final I msg) throws Exception {
        this.read2(ctx, msg);
        return null;
    }

    protected abstract void read2(final ChannelHandlerContext ctx, final I msg) throws Exception;

}
