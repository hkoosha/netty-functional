package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


public abstract class MatchedOutboundHandler<O> extends ChannelOutboundHandlerAdapter {

    private final TypeParameterMatcher typeMatcher;
    private final Matcher matcher;

    protected MatchedOutboundHandler() {

        this.typeMatcher = TypeParameterMatcher.find(
                this, MatchedOutboundHandler.class, "T");

        this.matcher = new Matcher() {
            @Override
            public Boolean apply(final Object msg) {
                return MatchedOutboundHandler.this.typeMatcher.match(msg);
            }
        };
    }

    protected MatchedOutboundHandler(@NonNull final Class<?> type) {

        this(MatcherUtil.classMatcher(type));
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

        if (this.matches(msg) && this.accepts(ctx, (O) msg))
            this.write0(ctx, (O) msg, promise);
        else
            this.unsupportedMsg(ctx, msg, promise);
    }


    protected boolean matches(@NonNull final Object msg) throws Exception {

        return this.matcher.apply(msg);
    }

    protected boolean accepts(final ChannelHandlerContext ctx, final O msg) {

        return true;
    }


    protected abstract void write0(
            ChannelHandlerContext ctx, O msg, ChannelPromise promise) throws Exception;

    protected abstract void unsupportedMsg(
            ChannelHandlerContext ctx, Object msg, ChannelPromise promise);
}
