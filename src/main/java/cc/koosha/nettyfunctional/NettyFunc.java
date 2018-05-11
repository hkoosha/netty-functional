package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.checkedfunction.ActionC;
import cc.koosha.nettyfunctional.checkedfunction.ConsumerC;
import cc.koosha.nettyfunctional.log.NettyFuncLogger;
import cc.koosha.nettyfunctional.log.SerrLogger;
import cc.koosha.nettyfunctional.log.Slf4jNettyFuncLogger;
import cc.koosha.nettyfunctional.nettyfunctions.Write;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.util.Objects;
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
            final SerrLogger l = new SerrLogger();
            l.error("slf4j not found", e);
            logger = l;
        }
    }

    private static final ActionC DO_NOTHING = () -> {
    };
    private static final Consumer<Throwable> THROW_LOG_AWAY = throwable -> logger.warn("error", throwable);
    private static final ConsumerC<Channel> THROW_CHANNEL_AWAY = channel -> {
    };

    @Sharable
    private static final class InitChannel extends ChannelInitializer<Channel> {

        private final ConsumerC<Channel> apply;

        private InitChannel(final ConsumerC<Channel> apply) {
            this.apply = apply;
        }

        protected void initChannel(final Channel ch) throws Exception {

            this.apply.accept(ch);
        }

    }

    @Sharable
    private static final class OnBind extends ChannelOutboundHandlerAdapter {

        private final Write<SocketAddress> apply;

        private OnBind(final Write<SocketAddress> apply) {
            this.apply = apply;
        }

        @Override
        public void bind(final ChannelHandlerContext ctx,
                         final SocketAddress localAddress,
                         final ChannelPromise promise) throws Exception {

            apply.accept(ctx, localAddress, promise);

            ctx.pipeline().remove(this);
        }

    }

    // _________________________________________________________________________

    public static ChannelHandler initer(final ConsumerC<Channel> accept) {
        Objects.requireNonNull(accept, "initer");
        return new NettyFunc.InitChannel(accept);

    }

    public static ChannelOutboundHandler onBind(final Write<SocketAddress> accept) {
        Objects.requireNonNull(accept, "onBind");
        return new OnBind(accept);
    }

    // _________________________________________________________________________

    public static ChannelFuture safe(Channel channel, Supplier<ChannelFuture> c) {
        return safe(channel, c, DO_NOTHING, THROW_LOG_AWAY);
    }

    public static ChannelFuture safe(Channel channel, Supplier<ChannelFuture> c, ActionC listener) {
        return safe(channel, c, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture safer(Channel channel, Supplier<ChannelFuture> c, Consumer<Throwable> onErr) {
        return safe(channel, c, DO_NOTHING, onErr);
    }

    public static ChannelFuture safe(Channel channel, Supplier<ChannelFuture> cf, ActionC listener, Consumer<Throwable> onErr) {
        if (channel == null)
            throw new NullPointerException("channel");
        if (cf == null)
            throw new NullPointerException("ChannelFuture creator (the operation)");
        if (listener == null)
            throw new NullPointerException("listener (for ChannelFuture)");
        if (onErr == null)
            throw new NullPointerException("onErr (error handler for ChannelFuture)");

        ChannelFuture cf1 = null;
        try {
            cf1 = cf.get();
            final ChannelFuture channelFuture = cf1;
            cf1.addListener(f -> {
                try {
                    if (!f.isSuccess()) {
                        close(channel);
                        close(channelFuture);
                        onErr.accept(f.cause());
                    }
                    else {
                        try {
                            listener.exec();
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
                    onErr.accept(e);
                }
            });
        }
        catch (Exception e) {
            close(channel);
            close(cf1);
            onErr.accept(e);
        }
        return cf1;
    }


    public static ChannelFuture write(ChannelHandlerContext c, Object payload) {
        return write(c.channel(), payload);
    }

    public static ChannelFuture write(ChannelHandlerContext c, Object payload, ActionC listener) {
        return write(c.channel(), payload, listener);
    }

    public static ChannelFuture writer(ChannelHandlerContext c, Object payload, Consumer<Throwable> onErr) {
        return writer(c.channel(), payload, onErr);
    }

    public static ChannelFuture write(ChannelHandlerContext c, Object payload, ActionC listener, Consumer<Throwable> onErr) {
        return write(c.channel(), payload, listener, onErr);
    }


    public static ChannelFuture write(Channel c, Object payload) {
        return write(c, payload, DO_NOTHING, THROW_LOG_AWAY);
    }

    public static ChannelFuture write(Channel c, Object payload, ActionC listener) {
        return write(c, payload, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture writer(Channel c, Object payload, Consumer<Throwable> onErr) {
        return write(c, payload, DO_NOTHING, onErr);
    }

    public static ChannelFuture write(Channel c, Object payload, ActionC listener, Consumer<Throwable> onErr) {
        return safe(c, () -> c.writeAndFlush(payload), listener, throwable -> {
            if (ReferenceCountUtil.refCnt(payload) > 0)
                ReferenceCountUtil.release(payload);
            onErr.accept(throwable);
        });
    }


    public static ChannelFuture write(Bootstrap bootstrap, Object payload) {
        return write(bootstrap, payload, THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static ChannelFuture write(Bootstrap bootstrap, Object payload, ConsumerC<Channel> listener) {
        return write(bootstrap, payload, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture writer(Bootstrap bootstrap, Object payload, Consumer<Throwable> onErr) {
        return write(bootstrap, payload, THROW_CHANNEL_AWAY, onErr);
    }

    public static ChannelFuture write(Bootstrap bootstrap, Object payload, ConsumerC<Channel> listener, Consumer<Throwable> onErr) {
        return connect(bootstrap, channel -> write(channel, payload, () -> listener.accept(channel), throwable -> {
            if (ReferenceCountUtil.refCnt(payload) > 0)
                ReferenceCountUtil.release(payload);
            onErr.accept(throwable);
        }), throwable -> {
            if (ReferenceCountUtil.refCnt(payload) > 0)
                ReferenceCountUtil.release(payload);
            onErr.accept(throwable);
        });
    }


    public static ChannelFuture connect(Bootstrap bootstrap) {
        return connect(bootstrap, THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static ChannelFuture connect(Bootstrap bootstrap, ConsumerC<Channel> listener) {
        return connect(bootstrap, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture connecter(Bootstrap bootstrap, Consumer<Throwable> onErr) {
        return connect(bootstrap, THROW_CHANNEL_AWAY, onErr);
    }

    public static ChannelFuture connect(Bootstrap bootstrap, ConsumerC<Channel> listener, Consumer<Throwable> onErr) {
        if (listener == null)
            throw new NullPointerException("listener (onConnect)");
        if (onErr == null)
            throw new NullPointerException("listener (onConnectError)");

        ChannelFuture cf = null;
        try {
            cf = bootstrap.connect();
            final ChannelFuture channelFuture = cf;
            cf.addListener(f -> {
                try {
                    if (!f.isSuccess()) {
                        close(channelFuture);
                        onErr.accept(f.cause());
                    }
                    else {
                        try {
                            listener.accept(channelFuture.channel());
                        }
                        catch (Exception e) {
                            close(channelFuture);
                            onErr.accept(e);
                        }
                    }
                }
                catch (Exception e) {
                    close(channelFuture);
                    onErr.accept(e);
                }
            });
        }
        catch (Exception e) {
            close(cf);
            onErr.accept(e);
        }
        return cf;
    }


    // _________________________________________________________________________

    public static boolean close(ChannelHandlerContext ctx) {
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


    public static <T> T closeN(ChannelHandlerContext ctx) {
        close(ctx);
        return null;
    }

    public static <T> T closeN(ChannelFuture c) {
        close(c);
        return null;
    }

    public static <T> T closeN(Channel channel) {
        close(channel);
        return null;
    }

    public static <T> T closeN(ChannelHandlerContext ctx, Object payload) {
        close(ctx, payload);
        return null;
    }

}

