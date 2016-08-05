package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.hook.ExceptionHook;
import cc.koosha.nettyfunctional.hook.ExceptionTransform;
import cc.koosha.nettyfunctional.nettyfunctions.ExceptionTransformer;
import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import cc.koosha.nettyfunctional.nettyfunctions.Read;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;


public enum Exceptions {
    ;

    public static <T extends Throwable> ChannelHandler exHook(@NonNull final Matcher matcher,
                                                              @NonNull final Read<T> handler) {

        return new ExceptionHook<T>(matcher) {

            @Override
            public boolean isSharable() {
                return true;
            }

            @Override
            protected void exception1(final ChannelHandlerContext ctx,
                                      final T exception) throws Exception {

                handler.accept(ctx, (T) exception);
            }
        };
    }

    public static <T extends Throwable> ChannelHandler exTransform(@NonNull final Matcher matcher,
                                                                   @NonNull final ExceptionTransformer<T> handler) {

        return new ExceptionTransform<T>(matcher) {
            @Override
            protected Throwable exception1(final ChannelHandlerContext ctx,
                                           final T exception) throws Exception {

                return handler.apply(ctx, exception);
            }
        };

    }

    public static <T extends Throwable> ChannelHandler exSink(@NonNull final Matcher matcher,
                                                              @NonNull final Read<T> handler) {

        return new ExceptionTransform<T>(matcher) {
            @Override
            protected Throwable exception1(final ChannelHandlerContext ctx,
                                           final T exception) throws Exception {
                handler.accept(ctx, exception);
                return null;
            }
        };
    }

    // ________________________________________________________________________

    public static <T extends Throwable> ChannelHandler exHook(@NonNull final Class<? extends Throwable> matcher,
                                                              @NonNull final Read<T> handler) {

        return exHook(Matcher.classMatcher(matcher), handler);
    }

    public static <T extends Throwable> ChannelHandler exTransform(@NonNull final Class<? extends Throwable> matcher,
                                                                   @NonNull final ExceptionTransformer<T> handler) {

        return exTransform(Matcher.classMatcher(matcher), handler);
    }

    public static <T extends Throwable> ChannelHandler exSink(@NonNull final Class<? extends Throwable> matcher,
                                                              @NonNull final Read<T> handler) {

        return exSink(Matcher.classMatcher(matcher), handler);
    }

}
