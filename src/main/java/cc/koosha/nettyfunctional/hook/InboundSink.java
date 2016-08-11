package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class InboundSink<T> extends InboundTransformer<T> {

    public InboundSink() {
    }

    public InboundSink(@NonNull final Class<?> clazz) {
        super(clazz);
    }

    public InboundSink(@NonNull final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Object read1(final ChannelHandlerContext ctx,
                                 final T msg) throws Exception {

        this.read2(ctx, msg);
        return null;
    }

    protected abstract void read2(final ChannelHandlerContext ctx, final T msg) throws Exception;

}
