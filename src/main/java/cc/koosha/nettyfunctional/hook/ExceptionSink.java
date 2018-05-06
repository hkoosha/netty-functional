package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class ExceptionSink<T extends Throwable> extends ExceptionTransform<T> {

    public ExceptionSink() {
        super();
    }

    public ExceptionSink(final Class<?> type) {
        super(type);
    }

    public ExceptionSink(final Matcher matcher) {
        super(matcher);
    }

    @Override
    protected final Throwable exception1(final ChannelHandlerContext ctx,
                                         final T exception) throws Exception {
        this.exception2(ctx, exception);
        return null;
    }

    protected abstract void exception2(final ChannelHandlerContext ctx, final T exception) throws Exception;

}
