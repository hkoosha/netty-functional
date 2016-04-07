package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.nettyfunctions.IfRead;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Read;
import cc.koosha.nettyfunctional.nettyfunctions.ReadTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;



public enum Inbound {
    ;

    public static <T> ChannelHandler hook(@NonNull final Matcher matcher,
                                          @NonNull final Read<T> handler) {

        return new InboundHook<T>(matcher) {
            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                 final T read) throws Exception {

                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler transform(@NonNull final Matcher matcher,
                                               @NonNull final ReadTransformer<T> handler) {

        return new InboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                    final T read) throws Exception {

                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler rmHook(@NonNull final Matcher matcher,
                                            @NonNull final Read<T> handler) {

        return new RemovedInboundHook<T>(matcher) {
            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                  final T read) throws Exception {
                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler rmSink(@NonNull final Matcher matcher,
                                            @NonNull final Read<T> handler) {

        return new RemovedInboundSink<T>(matcher) {
            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                 final T read) throws Exception {
                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Matcher matcher,
                                                 @NonNull final ReadTransformer<T> handler) {

        return new RemovedInboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {
                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler rmSinkIf(@NonNull final Matcher matcher,
                                              @NonNull final IfRead<T> handler) {

        return new RemovedIfInboundSink<T>(matcher) {
            @Override
            protected boolean read1(final ChannelHandlerContext ctx,
                                    final T msg) throws Exception {

                return handler.apply(ctx, msg);
            }
        };
    }


    // ________________________________________________________________________

    public static <T> ChannelHandler hook(@NonNull final Class<? extends T> matcher,
                                          @NonNull final Read<T> handler) {

        return hook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler transform(@NonNull final Class<? extends T> matcher,
                                               @NonNull final ReadTransformer<T> handler) {

        return transform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmHook(@NonNull final Class<? extends T> matcher,
                                            @NonNull final Read<T> handler) {

        return rmHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmSink(@NonNull final Class<? extends T> matcher,
                                            @NonNull final Read<T> handler) {

        return rmSink(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmTransform(@NonNull final Class<? extends T> matcher,
                                                 @NonNull final ReadTransformer<T> handler) {

        return rmTransform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler rmSinkIf(@NonNull final Class<? extends T> matcher,
                                              @NonNull final IfRead<T> handler) {

        return rmSinkIf(Matcher.classMatcher(matcher), handler);
    }

}
