package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.matched.MatchedExceptionHandler;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public abstract class ExceptionTransform<X extends Throwable> extends MatchedExceptionHandler<X> {

    protected ExceptionTransform() {

        super();
    }

    protected ExceptionTransform(@NonNull final Class<?> type) {

        super(type);
    }

    protected ExceptionTransform(@NonNull final Matcher matcher) {

        super(matcher);
    }


    @Override
    protected void unsupportedException(final ChannelHandlerContext ctx,
                                        final Throwable exception) {

        ctx.fireExceptionCaught(exception);
    }

    @Override
    protected void exception0(final ChannelHandlerContext ctx,
                              final X exception) throws Exception {

        final Throwable result = this.exception1(ctx, exception);

        if(result != null)
            ctx.fireExceptionCaught(result);
    }

    protected abstract Throwable exception1(final ChannelHandlerContext ctx, final X exception) throws Exception;

}
