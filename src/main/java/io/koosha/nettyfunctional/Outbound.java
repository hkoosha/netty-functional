package io.koosha.nettyfunctional;

import io.koosha.nettyfunctional.hook.*;
import io.koosha.nettyfunctional.matched.MatcherUtil;
import io.koosha.nettyfunctional.nettyfunctions.IfWrite;
import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.koosha.nettyfunctional.nettyfunctions.Write;
import io.koosha.nettyfunctional.nettyfunctions.WriteTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Objects;


public final class Outbound {

    private Outbound() {

    }

    public static <T> ChannelHandler oHook(final Matcher matcher,
                                           final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new OutboundHook<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T event,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, event, promise);
            }
        };
    }

    public static <T> ChannelHandler oTransform(final Matcher matcher,
                                                final WriteTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new OutboundTransformer<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object write1(final ChannelHandlerContext ctx,
                                    final T event,
                                    final ChannelPromise promise) throws Exception {
                return handler.apply(ctx, event, promise);
            }
        };

    }

    public static <T> ChannelHandler oSink(final Matcher matcher,
                                           final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new OutboundSink<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void write2(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler oRmHook(final Matcher matcher,
                                             final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedOutboundHook<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler oRmSink(final Matcher matcher,
                                             final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedOutboundSink<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler oRmTransform(final Matcher matcher,
                                                  final WriteTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedOutboundTransformer<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object write1(final ChannelHandlerContext ctx,
                                    final T msg,
                                    final ChannelPromise promise) throws Exception {
                return handler.apply(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler oRmSinkIf(final Matcher matcher,
                                               final IfWrite<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedIfOutboundSink<T>(matcher) {
            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected boolean write1(final ChannelHandlerContext ctx,
                                     final T msg,
                                     final ChannelPromise promise) throws Exception {

                return handler.apply(ctx, msg, promise);
            }
        };
    }

    // ________________________________________________________________________

    public static <T> ChannelHandler oHook(final Class<? extends T> matcher,
                                           final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oTransform(final Class<? extends T> matcher,
                                                final WriteTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oTransform(MatcherUtil.classMatcher(matcher), handler);
    }


    public static <T> ChannelHandler oSink(final Class<? extends T> matcher,
                                           final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmHook(final Class<? extends T> matcher,
                                             final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oRmHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmSink(final Class<? extends T> matcher,
                                             final Write<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oRmSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmTransform(final Class<? extends T> matcher,
                                                  final WriteTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oRmTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmSinkIf(final Class<? extends T> matcher,
                                               final IfWrite<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return oRmSinkIf(MatcherUtil.classMatcher(matcher), handler);
    }

}

