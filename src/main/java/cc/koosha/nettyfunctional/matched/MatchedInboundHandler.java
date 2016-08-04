package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


@ChannelHandler.Sharable
public abstract class MatchedInboundHandler<T> extends ChannelInboundHandlerAdapter {

    private final TypeParameterMatcher typeMatcher;
    private final Matcher matcher;

    protected MatchedInboundHandler() {

        this.typeMatcher = TypeParameterMatcher.find(
                this, MatchedInboundHandler.class, "T");

        this.matcher = this.typeMatcher::match;
    }

    protected MatchedInboundHandler(@NonNull final Class<?> type) {

        this(Matcher.classMatcher(type));
    }

    protected MatchedInboundHandler(@NonNull final Matcher matcher) {

        this.typeMatcher = null;
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void channelRead(final ChannelHandlerContext ctx,
                                  final Object msg) throws Exception {

        if (this.matches(msg) && this.accepts((T) msg))
            this.read0(ctx, (T) msg);
        else
            this.unsupportedMsg(ctx, msg);
    }


    protected boolean matches(@NonNull final Object msg) throws Exception {

        return this.matcher.apply(msg);
    }

    protected boolean accepts(final T msg) {

        return true;
    }


    protected abstract void read0(ChannelHandlerContext ctx, T msg) throws Exception;

    protected abstract void unsupportedMsg(ChannelHandlerContext ctx, Object msg);

}
