package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


public abstract class OutboundSink<O> extends OutboundTransformer<O> {

    public OutboundSink() {
        super();
    }

    public OutboundSink(final Class<?> clazz) {
        super(clazz);
    }

    public OutboundSink(final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Object write1(final ChannelHandlerContext ctx,
                                  final O msg,
                                  final ChannelPromise promise) throws Exception {
        this.write2(ctx, msg, promise);
        return null;
    }

    protected abstract void write2(final ChannelHandlerContext ctx,
                                   final O msg,
                                   final ChannelPromise promise) throws Exception;

}
