package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


public abstract class MatchedOutboundHandler<T> extends ChannelOutboundHandlerAdapter {

    private final TypeParameterMatcher typeMatcher;
    private final Matcher matcher;

    protected MatchedOutboundHandler() {

        this.typeMatcher = TypeParameterMatcher.find(
                this, MatchedOutboundHandler.class, "T");

        this.matcher = this.typeMatcher::match;
    }

    protected MatchedOutboundHandler(@NonNull final Class<?> type) {

        this(Matcher.classMatcher(type));
    }

    protected MatchedOutboundHandler(@NonNull final Matcher matcher) {

        this.typeMatcher = null;
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void write(@NonNull final ChannelHandlerContext ctx,
                            final Object msg,
                            final ChannelPromise promise) throws Exception {

        if (this.matches(msg) && this.accepts(ctx, (T) msg))
            this.write0(ctx, (T) msg, promise);
        else
            this.unsupportedMsg(ctx, msg, promise);
    }


    protected boolean matches(@NonNull final Object msg) throws Exception {

        return this.matcher.apply(msg);
    }

    protected boolean accepts(final ChannelHandlerContext ctx, final T msg) {

        return true;
    }


    protected abstract void write0(
            ChannelHandlerContext ctx, T msg, ChannelPromise promise) throws Exception;

    protected abstract void unsupportedMsg(
            ChannelHandlerContext ctx, Object msg, ChannelPromise promise);
}
