package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Read;
import cc.koosha.nettyfunctional.nettyfunctions.ReadTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public enum Event {
    ;

    public static <T> ChannelHandler hook(@NonNull final Matcher matcher,
                                          @NonNull final Read<T> handler) {

        return new EventHook<T>(matcher) {
            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {

                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler transform(@NonNull final Matcher matcher,
                                               @NonNull final ReadTransformer<T> handler) {

        return new EventTransformer<T>(matcher) {
            @Override
            protected Object event1(final ChannelHandlerContext ctx,
                                    final T event) throws Exception {

                return handler.apply(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler rmHook(@NonNull final Matcher matcher,
                                            @NonNull final Read<T> handler) {

        return new RemovedEventHook<T>(matcher) {
            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {
                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler rmSink(@NonNull final Matcher matcher,
                                            @NonNull final Read<T> handler) {

        return new RemovedEventSink<T>(matcher) {
            @Override
            protected void event1(final ChannelHandlerContext ctx,
                                  final T event) throws Exception {
                handler.accept(ctx, event);
            }
        };
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Matcher matcher,
                                                 @NonNull final ReadTransformer<T> handler) {

        return new RemovedEventTransformer<T>(matcher) {
            @Override
            protected Object event1(final ChannelHandlerContext ctx,
                                    final T event) throws Exception {
                return handler.apply(ctx, event);
            }
        };
    }


    // ________________________________________________________________________

    public static <T> ChannelHandler hook(@NonNull final Class<?> matcher,
                                          @NonNull final Read<T> handler) {

        return hook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler transform(@NonNull final Class<?> matcher,
                                               @NonNull final ReadTransformer<T> handler) {

        return transform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmHook(@NonNull final Class<?> matcher,
                                            @NonNull final Read<T> handler) {

        return rmHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmSink(@NonNull final Class<?> matcher,
                                            @NonNull final Read<T> handler) {

        return rmSink(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Class<?> matcher,
                                                 @NonNull final ReadTransformer<T> handler) {

        return rmTransform(Matcher.classMatcher(matcher), handler);
    }

}
