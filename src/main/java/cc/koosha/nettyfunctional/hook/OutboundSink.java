package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class OutboundSink<T> extends OutboundTransformer<T> {

    public OutboundSink() {
    }

    public OutboundSink(@NonNull final Class<?> clazz) {
        super(clazz);
    }

    public OutboundSink(@NonNull final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Object write1(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {

        this.write2(ctx, msg, promise);
        return null;
    }

    protected abstract void write2(final ChannelHandlerContext ctx,
                                   final T msg,
                                   final ChannelPromise promise) throws Exception;

}
