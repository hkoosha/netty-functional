package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.matched.MatcherUtil;
import cc.koosha.nettyfunctional.nettyfunctions.IfWrite;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Write;
import cc.koosha.nettyfunctional.nettyfunctions.WriteTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public final class Outbound {

    private Outbound() {

    }

    public static <T> ChannelHandler oHook(@NonNull final Matcher matcher,
                                           @NonNull final Write<T> handler) {

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

    public static <T> ChannelHandler oTransform(@NonNull final Matcher matcher,
                                                @NonNull final WriteTransformer<T> handler) {

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

    public static <T> ChannelHandler oSink(@NonNull final Matcher matcher,
                                           @NonNull final Write<T> handler) {

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

    public static <T> ChannelHandler oRmHook(@NonNull final Matcher matcher,
                                             @NonNull final Write<T> handler) {

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

    public static <T> ChannelHandler oRmSink(@NonNull final Matcher matcher,
                                             @NonNull final Write<T> handler) {

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

    public static <T> ChannelHandler oRmTransform(@NonNull final Matcher matcher,
                                                  @NonNull final WriteTransformer<T> handler) {

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

    public static <T> ChannelHandler oRmSinkIf(@NonNull final Matcher matcher,
                                               @NonNull final IfWrite<T> handler) {

        return new RemovedIfOutboundSInk<T>(matcher) {

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

    public static <T> ChannelHandler oHook(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Write<T> handler) {

        return oHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oTransform(@NonNull final Class<? extends T> matcher,
                                                @NonNull final WriteTransformer<T> handler) {

        return oTransform(MatcherUtil.classMatcher(matcher), handler);
    }


    public static <T> ChannelHandler oSink(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Write<T> handler) {

        return oSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmHook(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Write<T> handler) {

        return oRmHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmSink(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Write<T> handler) {

        return oRmSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmTransform(@NonNull final Class<? extends T> matcher,
                                                  @NonNull final WriteTransformer<T> handler) {

        return oRmTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler oRmSinkIf(@NonNull final Class<? extends T> matcher,
                                               @NonNull final IfWrite<T> handler) {

        return oRmSinkIf(MatcherUtil.classMatcher(matcher), handler);
    }

}

