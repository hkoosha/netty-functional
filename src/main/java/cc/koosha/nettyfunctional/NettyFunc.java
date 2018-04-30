package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.checkedfunction.Action;
import cc.koosha.nettyfunctional.checkedfunction.ConsumerC;
import cc.koosha.nettyfunctional.log.NettyFuncLogger;
import cc.koosha.nettyfunctional.log.SerrLogger;
import cc.koosha.nettyfunctional.log.Slf4jNettyFuncLogger;
import cc.koosha.nettyfunctional.nettyfunctions.Write;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.net.SocketAddress;
import java.util.function.Consumer;
import java.util.function.Supplier;


@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public final class NettyFunc {

    private NettyFunc() {

    }

    private static NettyFuncLogger logger;

    static {
        // TODO make env configurable.
        try {
            NettyFunc.class.getClassLoader().loadClass("org.slf4j.Logger");
            logger = new Slf4jNettyFuncLogger();
        }
        catch (ClassNotFoundException e) {
            val l = new SerrLogger();
            l.error("slf4j not found", e);
            logger = l;
        }
    }


    private static final Consumer<Throwable> THROW_LOG_AWAY = throwable -> logger.warn("error", throwable);
    private static final Consumer<Channel> THROW_CHANNEL_AWAY = channel -> {
    };


    public static ChannelHandler initer(@NonNull final ConsumerC<Channel> accept) {

        return new NettyFunc.InitChannel(accept);

    }

    public static ChannelOutboundHandler onBind(@NonNull final Write<SocketAddress> accept) {

        return new OnBind(accept);
    }


    @Sharable
    @RequiredArgsConstructor
    private static final class InitChannel extends ChannelInitializer<Channel> {

        private final ConsumerC<Channel> apply;

        protected void initChannel(final Channel ch) throws Exception {

            this.apply.accept(ch);
        }

    }

    @Sharable
    @RequiredArgsConstructor
    private static final class OnBind extends ChannelOutboundHandlerAdapter {

        private final Write<SocketAddress> apply;

        @Override
        public void bind(final ChannelHandlerContext ctx,
                         final SocketAddress localAddress,
                         final ChannelPromise promise) throws Exception {

            apply.accept(ctx, localAddress, promise);

            ctx.pipeline().remove(this);
        }

    }


    // _________________________________________________________________________

    private static void _safe(Channel channel,
                              Supplier<ChannelFuture> c,
                              Consumer<Channel> listener,
                              Consumer<Throwable> onErr) {
        ChannelFuture cf = null;
        try {
            cf = c.get();
            val channelFuture = cf;
            cf.addListener(f -> {
                try {
                    if (!f.isSuccess()) {
                        close(channel);
                        close(channelFuture);
                        onErr.accept(f.cause());
                    }
                    else {
                        try {
                            listener.accept(channel == null ? channelFuture.channel() : channel);
                        }
                        catch (Exception e) {
                            close(channel);
                            close(channelFuture);
                            onErr.accept(e);
                        }
                    }
                }
                catch (Exception e) {
                    close(channel);
                    close(channelFuture);
                    throw e;
                }
            });
        }
        catch (Exception e) {
            close(channel);
            close(cf);
            throw e;
        }
    }


    public static void safe(Channel channel, Supplier<ChannelFuture> c, Consumer<Channel> listener) {
        _safe(channel, c, listener, THROW_LOG_AWAY);
    }

    public static void safer(Channel channel, Supplier<ChannelFuture> c, Consumer<Throwable> onErr) {
        _safe(channel, c, THROW_CHANNEL_AWAY, onErr);
    }

    public static void safe(Channel channel,
                            Supplier<ChannelFuture> c,
                            Consumer<Channel> listener,
                            Consumer<Throwable> onErr) {
        _safe(channel, c, listener, onErr);
    }


    public static void write(ChannelHandlerContext c, Object payload) {
        _safe(c.channel(), () -> c.writeAndFlush(payload), THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static void write(ChannelHandlerContext c, Object payload, Action listener) {
        _safe(c.channel(), () -> c.writeAndFlush(payload), channel -> listener.exec(), THROW_LOG_AWAY);
    }

    public static void writer(ChannelHandlerContext c, Object payload, Consumer<Throwable> onErr) {
        _safe(c.channel(), () -> c.writeAndFlush(payload), THROW_CHANNEL_AWAY, onErr);
    }

    public static void write(ChannelHandlerContext c, Object payload, Action listener, Consumer<Throwable> onErr) {
        _safe(c.channel(), () -> c.writeAndFlush(payload), channel -> listener.exec(), onErr);
    }


    public static void write(Channel c, Object payload) {
        _safe(c, () -> c.writeAndFlush(payload), THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static void write(Channel c, Object payload, Action listener) {
        _safe(c, () -> c.writeAndFlush(payload), channel -> listener.exec(), THROW_LOG_AWAY);
    }

    public static void writer(Channel c, Object payload, Consumer<Throwable> onErr) {
        _safe(c, () -> c.writeAndFlush(payload), THROW_CHANNEL_AWAY, onErr);
    }

    public static void write(Channel c, Object payload, Action listener, Consumer<Throwable> onErr) {
        _safe(c, () -> c.writeAndFlush(payload), channel -> listener.exec(), onErr);
    }


    public static void connect(Bootstrap bootstrap) {
        _safe(null, bootstrap::connect, THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static void connect(Bootstrap bootstrap, Consumer<Channel> listener) {
        _safe(null, bootstrap::connect, listener, THROW_LOG_AWAY);
    }

    public static void connecter(Bootstrap bootstrap, Consumer<Throwable> onErr) {
        _safe(null, bootstrap::connect, THROW_CHANNEL_AWAY, onErr);
    }

    public static void connect(Bootstrap bootstrap, Consumer<Channel> listener, Consumer<Throwable> onErr) {
        _safe(null, bootstrap::connect, listener, onErr);
    }


    public static void send(Bootstrap bootstrap, Object payload) {
        connect(bootstrap, channel -> write(channel, payload));
    }

    public static void send(Bootstrap bootstrap, Object payload, Consumer<Channel> listener) {
        connect(bootstrap, channel -> write(channel, payload, () -> listener.accept(channel)));
    }

    public static void sender(Bootstrap bootstrap, Object payload, Consumer<Throwable> onErr) {
        connect(bootstrap, channel -> writer(channel, payload, onErr), onErr);
    }

    public static void send(Bootstrap bootstrap,
                            Object payload,
                            Consumer<Channel> listener,
                            Consumer<Throwable> onErr) {
        connect(bootstrap, channel -> write(channel, payload, () -> listener.accept(channel), onErr), onErr);
    }

    // _________________________________________________________________________

    public static boolean close(final ChannelHandlerContext ctx) {
        try {
            if (ctx != null)
                ctx.close();
        }
        catch (Exception e) {
            logger.error("error while closing ChannelHandlerContext", e);
        }
        return false;
    }

    public static boolean close(ChannelFuture c) {
        try {
            if (c != null)
                c.channel().close();
        }
        catch (Exception e) {
            logger.error("error while closing channel", e);
        }
        return false;
    }

    public static boolean close(Channel channel) {
        try {
            if (channel != null && channel.isOpen())
                channel.close();
        }
        catch (Exception e) {
            logger.error("error while closing channel", e);
        }
        return false;
    }

    public static boolean close(ChannelHandlerContext ctx, Object payload) {
        write(ctx, payload, ctx::close, err ->
                logger.warn("could not write final payload to channel", err));
        return false;
    }


}

