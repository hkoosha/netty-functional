package io.koosha.nettyfunctional;

import io.koosha.nettyfunctional.hook.*;
import io.koosha.nettyfunctional.matched.MatcherUtil;
import io.koosha.nettyfunctional.nettyfunctions.IfRead;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.koosha.nettyfunctional.nettyfunctions.Read;
import io.koosha.nettyfunctional.nettyfunctions.ReadTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;


@SuppressWarnings({"WeakerAccess", "unused"})
public final class Event {

    private Event() {

    }

    public static <T> ChannelHandler eHook(final Matcher matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new EventHook<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {
                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eTransform(final Matcher matcher,
                                                final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new EventTransformer<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object event1(final ChannelHandlerContext ctx,
                                    final T event) throws Exception {

                return handler.apply(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eSink(final Matcher matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new EventSink<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void event2(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {

                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eRmHook(final Matcher matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new RemovedEventHook<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {
                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eRmSink(final Matcher matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new RemovedEventSink<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {
                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eRmTransform(final Matcher matcher,
                                                  final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new RemovedEventTransformer<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object event1(final ChannelHandlerContext ctx,
                                    final T event) throws Exception {
                return handler.apply(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler eRmSinkIf(final Matcher matcher,
                                               final IfRead<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return new RemovedIfEventSink<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected boolean event1(final ChannelHandlerContext ctx,
                                     final T event) throws Exception {

                return handler.apply(ctx, event);
            }
        };
    }

    // ________________________________________________________________________

    public static <T> ChannelHandler eHook(final Class<? extends T> matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eTransform(final Class<? extends T> matcher,
                                                final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eSink(final Class<? extends T> matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmHook(final Class<? extends T> matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eRmHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmSink(final Class<? extends T> matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eRmSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmTransform(final Class<? extends T> matcher,
                                                  final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eRmTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmSinkIf(final Class<? extends T> matcher,
                                               final IfRead<T> handler) {
        Objects.requireNonNull(matcher, "matcher");
        Objects.requireNonNull(handler, "handler");
        return eRmSinkIf(MatcherUtil.classMatcher(matcher), handler);
    }

}
