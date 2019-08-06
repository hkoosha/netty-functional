package io.koosha.nettyfunctional.matched;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.TypeParameterMatcher;

import java.util.Objects;

import static io.koosha.nettyfunctional.matched.MatcherUtil.release;


@SuppressWarnings({"unused", "WeakerAccess", "RedundantThrows"})
public abstract class MatchedInboundHandler<I> extends ChannelInboundHandlerAdapter {

    private final Matcher matcher;

    protected MatchedInboundHandler() {
        final TypeParameterMatcher typeMatcher = TypeParameterMatcher.find(
                this, MatchedInboundHandler.class, "I");
        this.matcher = new Matcher() {
            @Override
            public Boolean apply(Object msg) {
                return typeMatcher.match(msg);
            }
        };
    }

    protected MatchedInboundHandler(final Class<?> type) {
        this(MatcherUtil.classMatcher(type));
    }

    protected MatchedInboundHandler(final Matcher matcher) {
        Objects.requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void channelRead(final ChannelHandlerContext ctx,
                                  final Object msg) throws Exception {
        try {
            if (this.matches(msg) && this.accepts(ctx, ((I) msg)))
                this.read0(ctx, (I) msg);
            else
                this.unsupportedMsg(ctx, msg);
        }
        catch (Throwable e) {
            release(msg);
            throw e;
        }
    }


    protected boolean matches(final Object msg) throws Exception {
        return this.matcher.apply(msg);
    }

    protected boolean accepts(final ChannelHandlerContext ctx, final I msg) {
        return true;
    }


    protected abstract void read0(ChannelHandlerContext ctx, I msg) throws Exception;

    protected abstract void unsupportedMsg(ChannelHandlerContext ctx, Object msg);

}
