package io.koosha.nettyfunctional;

import io.koosha.nettyfunctional.hook.ExceptionHook;
import io.koosha.nettyfunctional.hook.ExceptionSink;
import io.koosha.nettyfunctional.hook.ExceptionTransform;
import io.koosha.nettyfunctional.matched.MatcherUtil;
import io.koosha.nettyfunctional.nettyfunctions.ExceptionTransformer;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.koosha.nettyfunctional.nettyfunctions.Read;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;


@SuppressWarnings({"WeakerAccess", "unused"})
public final class Exceptions {

    private Exceptions() {

    }

    public static <T extends Throwable> ChannelHandler exHook(final Matcher matcher,
                                                              final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new ExceptionHook<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void exception1(final ChannelHandlerContext ctx,
                                      final T exception) throws Exception {

                handler.accept(ctx, exception);
            }
        };
    }

    public static <T extends Throwable> ChannelHandler exTransform(final Matcher matcher,
                                                                   final ExceptionTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new ExceptionTransform<T>(matcher) {

            @Override
            public boolean isSharable() {

                return true;
            }

            @Override
            protected Throwable exception1(final ChannelHandlerContext ctx,
                                           final T exception) throws Exception {

                return handler.apply(ctx, exception);
            }
        };

    }

    public static <T extends Throwable> ChannelHandler exSink(final Matcher matcher,
                                                              final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new ExceptionSink<T>(matcher) {

            @Override
            public boolean isSharable() {

                return true;
            }

            @Override
            protected void exception2(final ChannelHandlerContext ctx,
                                      final T exception) throws Exception {
                handler.accept(ctx, exception);
            }
        };
    }

    // ________________________________________________________________________

    public static <T extends Throwable> ChannelHandler exHook(final Class<? extends Throwable> matcher,
                                                              final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return exHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T extends Throwable> ChannelHandler exTransform(final Class<? extends Throwable> matcher,
                                                                   final ExceptionTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return exTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T extends Throwable> ChannelHandler exSink(final Class<? extends Throwable> matcher,
                                                              final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return exSink(MatcherUtil.classMatcher(matcher), handler);
    }

}
