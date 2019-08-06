package io.koosha.nettyfunctional.hook;

import io.koosha.nettyfunctional.matched.MatchedExceptionHandler;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;


public abstract class ExceptionHook<X extends Throwable> extends MatchedExceptionHandler<X> {

    protected ExceptionHook() {
        super();
    }

    protected ExceptionHook(final Class<?> type) {
        super(type);
    }

    protected ExceptionHook(final Matcher matcher) {
        super(matcher);
    }


    @Override
    protected void unsupportedException(final ChannelHandlerContext ctx,
                                        final Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    protected void exception0(final ChannelHandlerContext ctx,
                              final X exception) throws Exception {
        this.exception1(ctx, exception);
        ctx.fireExceptionCaught(exception);
    }


    protected abstract void exception1(final ChannelHandlerContext ctx, final X exception) throws Exception;

}
