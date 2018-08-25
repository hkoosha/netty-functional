package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;

import java.util.Objects;


public abstract class MatchedEventHandler<E> extends ChannelDuplexHandler {

    private final Matcher matcher;

    public MatchedEventHandler() {
        final TypeParameterMatcher typeMatcher = TypeParameterMatcher.find(
                this, MatchedEventHandler.class, "E");
        this.matcher = new Matcher() {
            @Override
            public Boolean apply(Object msg) {
                return typeMatcher.match(msg);
            }
        };
    }

    public MatchedEventHandler(final Class<?> type) {
        this(MatcherUtil.classMatcher(type));
    }

    public MatchedEventHandler(final Matcher matcher) {
        Objects.requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void userEventTriggered(final ChannelHandlerContext ctx,
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
