package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class ExceptionSink<T extends Throwable> extends ExceptionTransform<T> {

    public ExceptionSink() {
        super();
    }

    public ExceptionSink(@NonNull final Class<?> type) {
        super(type);
    }

    public ExceptionSink(@NonNull final Matcher matcher) {
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
