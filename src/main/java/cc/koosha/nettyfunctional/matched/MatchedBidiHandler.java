package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;

import java.util.Objects;

import static cc.koosha.nettyfunctional.matched.MatcherUtil.release;


@SuppressWarnings({"unused", "WeakerAccess", "RedundantThrows"})
public abstract class MatchedBidiHandler<I, O> extends ChannelDuplexHandler {

    private final Matcher iMatcher;
    private final Matcher oMatcher;

    protected MatchedBidiHandler() {
        final TypeParameterMatcher iTypeMatcher = TypeParameterMatcher.find(
                this, MatchedBidiHandler.class, "I");

        final TypeParameterMatcher oTypeMatcher = TypeParameterMatcher.find(
                this, MatchedBidiHandler.class, "O");

        this.iMatcher = new Matcher() {
            @Override
            public Boolean apply(Object msg) {
                return iTypeMatcher.match(msg);
            }
        };
        this.oMatcher = new Matcher() {
            @Override
            public Boolean apply(Object msg) {
                return oTypeMatcher.match(msg);
            }
        };
    }

    protected MatchedBidiHandler(final Class<?> iType,
                                 final Class<?> oType) {
        this(MatcherUtil.classMatcher(iType), MatcherUtil.classMatcher(oType));
    }

    protected MatchedBidiHandler(final Matcher iMatcher,
                                 final Matcher oMatcher) {
        Objects.requireNonNull(iMatcher, "InboundMatcher");
        Objects.requireNonNull(oMatcher, "OutboundMatcher");
        this.iMatcher = iMatcher;
        this.oMatcher = oMatcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void channelRead(final ChannelHandlerContext ctx,
                                  final Object msg) throws Exception {
        try {
            if (this.iMatches(msg) && this.iAccepts(ctx, ((I) msg)))
                this.read0(ctx, (I) msg);
            else
                this.iUnsupportedMsg(ctx, msg);
        }
        catch (Throwable e) {
            release(msg);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void write(final ChannelHandlerContext ctx,
                            final Object msg,
                            final ChannelPromise promise) throws Exception {
        final boolean yes;
        try {
            yes = this.oMatches(msg) && this.oAccepts(ctx, ((O) msg));
        }
        catch (Throwable e) {
            release(msg);
            throw e;
        }

        if (yes)
            this.write0(ctx, (O) msg, promise);
        else
            try {
                this.oUnsupportedMsg(ctx, msg, promise);
            }
            catch (Throwable e) {
                release(msg);
                throw e;
            }
    }


    protected boolean iMatches(final Object msg) throws Exception {
        return this.iMatcher.apply(msg);
    }

    protected boolean iAccepts(final ChannelHandlerContext ctx, final I msg) {
        return true;
    }

    protected boolean oMatches(final Object msg) throws Exception {
        return this.oMatcher.apply(msg);
    }

    protected boolean oAccepts(final ChannelHandlerContext ctx, final O msg) {
        return true;
    }


    protected abstract void read0(ChannelHandlerContext ctx, I msg) throws Exception;

    protected abstract void write0(ChannelHandlerContext ctx, O msg, ChannelPromise promise) throws Exception;


    protected abstract void iUnsupportedMsg(ChannelHandlerContext ctx, Object msg);

    protected abstract void oUnsupportedMsg(ChannelHandlerContext ctx, Object msg, ChannelPromise promise);

}
