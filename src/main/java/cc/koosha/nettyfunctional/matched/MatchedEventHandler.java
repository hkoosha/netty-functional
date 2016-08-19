package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


public abstract class MatchedEventHandler<E> extends ChannelDuplexHandler {

    private final TypeParameterMatcher typeMatcher;
    private final Matcher matcher;

    public MatchedEventHandler() {

        this.typeMatcher = TypeParameterMatcher.find(
                this, MatchedEventHandler.class, "T");

        this.matcher = new Matcher() {
            @Override
            public Boolean apply(final Object msg) {
                return MatchedEventHandler.this.typeMatcher.match(msg);
            }
        };
    }

    public MatchedEventHandler(@NonNull final Class<?> type) {

        this(MatcherUtil.classMatcher(type));
    }

    public MatchedEventHandler(@NonNull final Matcher matcher) {

        this.typeMatcher = null;
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void userEventTriggered(@NonNull final ChannelHandlerContext ctx,
                                         final Object evt) throws Exception {

        if (this.matches(evt) && this.accepts(ctx, (E) evt))
            this.event0(ctx, (E) evt);
        else
            this.unsupportedEvent(ctx, evt);
    }


    protected boolean matches(final Object event) throws Exception {

        return this.matcher.apply(event);
    }

    protected boolean accepts(final ChannelHandlerContext ctx, final E event) {

        return true;
    }


    protected abstract void event0(ChannelHandlerContext ctx, E event) throws Exception;

    protected abstract void unsupportedEvent(ChannelHandlerContext ctx, Object event);

}
