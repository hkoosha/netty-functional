package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.matched.MatcherUtil;
import cc.koosha.nettyfunctional.nettyfunctions.IfRead;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Read;
import cc.koosha.nettyfunctional.nettyfunctions.ReadTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


@SuppressWarnings({"WeakerAccess", "unused"})
public final class Event {

    private Event() {

    }

    public static <T> ChannelHandler eHook(@NonNull final Matcher matcher,
                                           @NonNull final Read<T> handler) {

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

    public static <T> ChannelHandler eTransform(@NonNull final Matcher matcher,
                                                @NonNull final ReadTransformer<T> handler) {

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

    public static <T> ChannelHandler eSink(@NonNull final Matcher matcher,
                                           @NonNull final Read<T> handler) {

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

    public static <T> ChannelHandler eRmHook(@NonNull final Matcher matcher,
                                             @NonNull final Read<T> handler) {

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

    public static <T> ChannelHandler eRmSink(@NonNull final Matcher matcher,
                                             @NonNull final Read<T> handler) {

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

    public static <T> ChannelHandler eRmTransform(@NonNull final Matcher matcher,
                                                  @NonNull final ReadTransformer<T> handler) {

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

    public static <T> ChannelHandler eRmSinkIf(@NonNull final Matcher matcher,
                                               @NonNull final IfRead<T> handler) {

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

    public static <T> ChannelHandler eHook(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Read<T> handler) {

        return eHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eTransform(@NonNull final Class<? extends T> matcher,
                                                @NonNull final ReadTransformer<T> handler) {

        return eTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eSink(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Read<T> handler) {

        return eSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmHook(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Read<T> handler) {

        return eRmHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmSink(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Read<T> handler) {

        return eRmSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmTransform(@NonNull final Class<? extends T> matcher,
                                                  @NonNull final ReadTransformer<T> handler) {

        return eRmTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler eRmSinkIf(@NonNull final Class<? extends T> matcher,
                                               @NonNull final IfRead<T> handler) {

        return eRmSinkIf(MatcherUtil.classMatcher(matcher), handler);
    }

}
