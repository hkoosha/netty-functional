package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedExceptionHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


@ChannelHandler.Sharable
public abstract class ExceptionHook<T extends Throwable> extends MatchedExceptionHandler<T> {

    protected ExceptionHook() {

        super();
    }

    protected ExceptionHook(@NonNull final Class<?> type) {

        super(type);
    }

    protected ExceptionHook(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected void unsupportedException(final ChannelHandlerContext ctx,
                                        final Throwable cause) {

        ctx.fireExceptionCaught(cause);
    }

    @Override
    protected void exception0(final ChannelHandlerContext ctx,
                              final T exception) throws Exception {

        this.exception1(ctx, exception);
        ctx.fireExceptionCaught(exception);
    }


    protected abstract void exception1(final ChannelHandlerContext ctx, final T exception) throws Exception;

}
