package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


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
            public Boolean apply(final Object msg) {
                return iTypeMatcher.match(msg);
            }
        };

        this.oMatcher = new Matcher() {
            @Override
            public Boolean apply(final Object msg) {
                return oTypeMatcher.match(msg);
            }
        };
    }

    protected MatchedBidiHandler(@NonNull final Class<?> iType,
                                 @NonNull final Class<?> oType) {

        this(MatcherUtil.classMatcher(iType), MatcherUtil.classMatcher(oType));
    }

    protected MatchedBidiHandler(@NonNull final Matcher iMatcher,
                                 @NonNull final Matcher oMatcher) {

        this.iMatcher = iMatcher;
        this.oMatcher = oMatcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void channelRead(final ChannelHandlerContext ctx,
                                  final Object msg) throws Exception {

        if (this.iMatches(msg) && this.iAccepts(ctx, (I) msg))
            this.read0(ctx, (I) msg);
        else
            this.iUnsupportedMsg(ctx, msg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void write(final ChannelHandlerContext ctx,
                            final Object msg,
                            final ChannelPromise promise) throws Exception {

        if (this.oMatches(msg) && this.oAccepts(ctx, (O) msg))
            this.write0(ctx, (O) msg, promise);
        else
            this.oUnsupportedMsg(ctx, msg, promise);
    }


    protected boolean iMatches(@NonNull final Object msg) throws Exception {

        return this.iMatcher.apply(msg);
    }

    protected boolean iAccepts(final ChannelHandlerContext ctx, final I msg) {

        return true;
    }

    protected boolean oMatches(@NonNull final Object msg) throws Exception {

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
