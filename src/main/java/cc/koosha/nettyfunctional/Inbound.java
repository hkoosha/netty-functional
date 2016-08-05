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

    public static <T> ChannelHandler iHook(@NonNull final Matcher matcher,
                                           @NonNull final Read<T> handler) {

        return new InboundHook<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                 final T read) throws Exception {

                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iTransform(@NonNull final Matcher matcher,
                                                @NonNull final ReadTransformer<T> handler) {

        return new InboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                    final T read) throws Exception {

                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iSink(@NonNull final Matcher matcher,
                                           @NonNull final Read<T> handler) {

        return new InboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {
                handler.accept(ctx, read);
                return null;
            }
        };
    }

    public static <T> ChannelHandler iRmHook(@NonNull final Matcher matcher,
                                             @NonNull final Read<T> handler) {

        return new RemovedInboundHook<T>(matcher) {
            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                  final T read) throws Exception {
                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iRmSink(@NonNull final Matcher matcher,
                                             @NonNull final Read<T> handler) {

        return new RemovedInboundSink<T>(matcher) {
            @Override
            protected void read1(final ChannelHandlerContext ctx,
                                 final T read) throws Exception {
                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iRmTransform(@NonNull final Matcher matcher,
                                                  @NonNull final ReadTransformer<T> handler) {

        return new RemovedInboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {
                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iRmSinkIf(@NonNull final Matcher matcher,
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

    public static <T> ChannelHandler iHook(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Read<T> handler) {

        return iHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iTransform(@NonNull final Class<? extends T> matcher,
                                                @NonNull final ReadTransformer<T> handler) {

        return iTransform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iSink(@NonNull final Class<? extends T> matcher,
                                           @NonNull final Read<T> handler) {

        return new InboundTransformer<T>(matcher) {
            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {
                handler.accept(ctx, read);
                return null;
            }
        };
    }

    public static <T> ChannelHandler iRmHook(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Read<T> handler) {

        return iRmHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmSink(@NonNull final Class<? extends T> matcher,
                                             @NonNull final Read<T> handler) {

        return iRmSink(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmTransform(@NonNull final Class<? extends T> matcher,
                                                  @NonNull final ReadTransformer<T> handler) {

        return iRmTransform(Matcher.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmSinkIf(@NonNull final Class<? extends T> matcher,
                                               @NonNull final IfRead<T> handler) {

        return iRmSinkIf(Matcher.classMatcher(matcher), handler);
    }

}
