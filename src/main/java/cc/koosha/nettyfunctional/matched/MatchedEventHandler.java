package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.TypeParameterMatcher;
import lombok.NonNull;


@ChannelHandler.Sharable
public abstract class MatchedEventHandler<T> extends ChannelDuplexHandler {

    private final TypeParameterMatcher typeMatcher;
    private final Matcher matcher;

    public MatchedEventHandler() {

        this.typeMatcher = TypeParameterMatcher.find(
                this, MatchedEventHandler.class, "T");

        this.matcher = this.typeMatcher::match;
    }

    public MatchedEventHandler(@NonNull final Class<?> type) {

        this(Matcher.classMatcher(type));
    }

    public MatchedEventHandler(@NonNull final Matcher matcher) {

        this.typeMatcher = null;
        this.matcher = matcher;
    }


    @SuppressWarnings("unchecked")
    @Override
    public final void userEventTriggered(@NonNull final ChannelHandlerContext ctx,
                                         final Object evt) throws Exception {

        if (this.matches(evt) && this.accepts((T) evt))
            this.event0(ctx, (T) evt);
        else
            this.unsupportedEvent(ctx, evt);
    }


    protected boolean matches(final Object event) throws Exception {

        return this.matcher.apply(event);
    }

    protected boolean accepts(final T event) {

        return true;
    }


    protected abstract void event0(ChannelHandlerContext ctx, T event) throws Exception;

    protected abstract void unsupportedEvent(ChannelHandlerContext ctx, Object event);

}
