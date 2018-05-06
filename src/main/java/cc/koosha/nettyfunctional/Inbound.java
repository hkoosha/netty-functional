package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.*;
import cc.koosha.nettyfunctional.matched.MatcherUtil;
import cc.koosha.nettyfunctional.nettyfunctions.IfRead;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Read;
import cc.koosha.nettyfunctional.nettyfunctions.ReadTransformer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;


@SuppressWarnings({"WeakerAccess", "unused"})
public final class Inbound {

    private Inbound() {

    }

    public static <T> ChannelHandler iHook(final Matcher matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
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

    public static <T> ChannelHandler iTransform(final Matcher matcher,
                                                final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new InboundTransformer<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {

                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iSink(final Matcher matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new InboundSink<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void read2(final ChannelHandlerContext ctx,
                                 final T read) throws Exception {
                handler.accept(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iRmHook(final Matcher matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedInboundHook<T>(matcher) {

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

    public static <T> ChannelHandler iRmSink(final Matcher matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedInboundSink<T>(matcher) {

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

    public static <T> ChannelHandler iRmTransform(final Matcher matcher,
                                                  final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedInboundTransformer<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected Object read1(final ChannelHandlerContext ctx,
                                   final T read) throws Exception {
                return handler.apply(ctx, read);
            }
        };
    }

    public static <T> ChannelHandler iRmSinkIf(final Matcher matcher,
                                               final IfRead<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return new RemovedIfInboundSink<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected boolean read1(final ChannelHandlerContext ctx,
                                    final T msg) throws Exception {

                return handler.apply(ctx, msg);
            }
        };
    }


    // ________________________________________________________________________

    public static <T> ChannelHandler iHook(final Class<? extends T> matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iTransform(final Class<? extends T> matcher,
                                                final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iSink(final Class<? extends T> matcher,
                                           final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmHook(final Class<? extends T> matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iRmHook(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmSink(final Class<? extends T> matcher,
                                             final Read<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iRmSink(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmTransform(final Class<? extends T> matcher,
                                                  final ReadTransformer<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iRmTransform(MatcherUtil.classMatcher(matcher), handler);
    }

    public static <T> ChannelHandler iRmSinkIf(final Class<? extends T> matcher,
                                               final IfRead<T> handler) {
        Objects.requireNonNull(matcher);
        Objects.requireNonNull(handler);
        return iRmSinkIf(MatcherUtil.classMatcher(matcher), handler);
    }

}
