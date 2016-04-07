package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.nettyfunctions.IfWrite;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Write;
import cc.koosha.nettyfunctional.nettyfunctions.WriteTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum  Outbound {
    ;

    public static <T> ChannelHandler hook(@NonNull final Matcher matcher,
                                          @NonNull final Write<T> handler) {

        return new OutboundHook<T>(matcher) {
            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T event,
                                  final ChannelPromise promise) throws Exception {

                handler.accept(ctx, event, promise);
            }
        };
    }

    public static <T> ChannelHandler transform(@NonNull final Matcher matcher,
                                               @NonNull final WriteTransformer<T> handler) {

        return new OutboundTransformer<T>(matcher) {
            @Override
            protected Object write1(final ChannelHandlerContext ctx,
                                    final T event,
                                    final ChannelPromise promise) throws Exception {

                return handler.apply(ctx, event, promise);
            }
        };

    }

    public static <T> ChannelHandler rmHook(@NonNull final Matcher matcher,
                                            @NonNull final Write<T> handler) {

        return new RemovedOutboundHook<T>(matcher) {
            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler rmSink(@NonNull final Matcher matcher,
                                            @NonNull final Write<T> handler) {

        return new RemovedOutboundSink<T>(matcher) {
            @Override
            protected void write1(final ChannelHandlerContext ctx,
                                  final T msg,
                                  final ChannelPromise promise) throws Exception {
                handler.accept(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Matcher matcher,
                                                 @NonNull final WriteTransformer<T> handler) {

        return new RemovedOutboundTransformer<T>(matcher) {
            @Override
            protected Object write1(final ChannelHandlerContext ctx,
                                    final T msg,
                                    final ChannelPromise promise) throws Exception {
                return handler.apply(ctx, msg, promise);
            }
        };
    }

    public static <T> ChannelHandler rmSinkIf(@NonNull final Matcher matcher,
                                              @NonNull final IfWrite<T> handler) {

        return new RemovedIfOutboundSInk<T>(matcher) {
            @Override
            protected boolean write1(final ChannelHandlerContext ctx,
                                     final T msg,
                                     final ChannelPromise promise) throws Exception {

                return handler.apply(ctx, msg, promise);
            }
        };
    }

    // ________________________________________________________________________

    public static <T> ChannelHandler hook(@NonNull final Class<? extends T> matcher,
                                          @NonNull final Write<T> handler) {

        return hook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler transform(@NonNull final Class<? extends T> matcher,
                                               @NonNull final WriteTransformer<T> handler) {

        return transform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmHook(@NonNull final Class<? extends T> matcher,
                                            @NonNull final Write<T> handler) {

        return rmHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmSink(@NonNull final Class<? extends T> matcher,
                                            @NonNull final Write<T> handler) {

        return rmSink(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Class<? extends T> matcher,
                                                 @NonNull final WriteTransformer<T> handler) {

        return rmTransform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmSinkIf(@NonNull final Class<? extends T> matcher,
                                              @NonNull final IfWrite<T> handler) {

        return rmSinkIf(Matcher.classMatcher(matcher), handler);
    }

}

