package io.koosha.nettyfunctional.matched;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;

import java.util.Objects;


public abstract class MatchedExceptionHandler<X extends Throwable> extends ChannelDuplexHandler {

    private final Matcher matcher;

    public MatchedExceptionHandler() {
        final TypeParameterMatcher typeMatcher = TypeParameterMatcher.find(
                this, MatchedExceptionHandler.class, "X");
        this.matcher = new Matcher() {
            @Override
            public Boolean apply(Object msg) {
                return typeMatcher.match(msg);
            }
        };
    }

    public MatchedExceptionHandler(final Class<?> type) {
        this(MatcherUtil.classMatcher(type));
    }

    public MatchedExceptionHandler(final Matcher matcher) {
        Objects.requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx,
                                      final Throwable cause) throws Exception {
        if (this.matches(cause) && this.accepts(ctx, (X) cause))
            this.exception0(ctx, (X) cause);
        else
            this.unsupportedException(ctx, cause);
    }

    protected boolean matches(final Throwable exception) throws Exception {
        return this.matcher.apply(exception);
    }

    protected boolean accepts(final ChannelHandlerContext ctx, final X exception) {
        return true;
    }


    protected abstract void exception0(ChannelHandlerContext ctx, X exception) throws Exception;

    protected abstract void unsupportedException(ChannelHandlerContext ctx, Throwable exception);

}
