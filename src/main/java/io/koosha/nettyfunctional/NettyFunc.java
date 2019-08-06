package io.koosha.nettyfunctional;

import io.koosha.nettyfunctional.checkedfunction.ActionC;
import io.koosha.nettyfunctional.checkedfunction.ConsumerC;
import io.koosha.nettyfunctional.log.NettyFuncLogger;
import io.koosha.nettyfunctional.log.SerrLogger;
import io.koosha.nettyfunctional.log.Slf4jNettyFuncLogger;
import io.koosha.nettyfunctional.nettyfunctions.Consumer2;
import io.koosha.nettyfunctional.nettyfunctions.Supplier2;
import io.koosha.nettyfunctional.nettyfunctions.Write;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.SocketAddress;
import java.util.Objects;


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

    private static final ActionC DO_NOTHING = new ActionC() {
        @Override
        public void exec() throws Exception {
        }
    };
    private static final Consumer2<Throwable> THROW_LOG_AWAY = new Consumer2<Throwable>() {
        @Override
        public void accept(Throwable throwable) {
            logger.warn("error", throwable);
        }
    };
    private static final ConsumerC<Channel> THROW_CHANNEL_AWAY = new ConsumerC<Channel>() {
        @Override
        public void accept(Channel channel) throws Exception {
        }
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

    public static ChannelFuture safe(Channel channel, Supplier2<ChannelFuture> c) {
        return safe(channel, c, DO_NOTHING, THROW_LOG_AWAY);
    }

    public static ChannelFuture safe(Channel channel, Supplier2<ChannelFuture> c, ActionC listener) {
        return safe(channel, c, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture safer(Channel channel, Supplier2<ChannelFuture> c, Consumer2<Throwable> onErr) {
        return safe(channel, c, DO_NOTHING, onErr);
    }

    public static ChannelFuture safe(final Channel channel, Supplier2<ChannelFuture> cf, final ActionC listener, final Consumer2<Throwable> onErr) {
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
            cf1.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> f) throws Exception {
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

    public static ChannelFuture writer(ChannelHandlerContext c, Object payload, Consumer2<Throwable> onErr) {
        return writer(c.channel(), payload, onErr);
    }

    public static ChannelFuture write(ChannelHandlerContext c, Object payload, ActionC listener, Consumer2<Throwable> onErr) {
        return write(c.channel(), payload, listener, onErr);
    }


    public static ChannelFuture write(Channel c, Object payload) {
        return write(c, payload, DO_NOTHING, THROW_LOG_AWAY);
    }

    public static ChannelFuture write(Channel c, Object payload, ActionC listener) {
        return write(c, payload, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture writer(Channel c, Object payload, Consumer2<Throwable> onErr) {
        return write(c, payload, DO_NOTHING, onErr);
    }

    public static ChannelFuture write(final Channel c, final Object payload, ActionC listener, final Consumer2<Throwable> onErr) {
        return safe(c, new Supplier2<ChannelFuture>() {
            @Override
            public ChannelFuture get() {
                return c.writeAndFlush(payload);
            }
        }, listener, new Consumer2<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                if (ReferenceCountUtil.refCnt(payload) > 0)
                    ReferenceCountUtil.release(payload);
                onErr.accept(throwable);
            }
        });
    }


    public static ChannelFuture write(Bootstrap bootstrap, Object payload) {
        return write(bootstrap, payload, THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static ChannelFuture write(Bootstrap bootstrap, Object payload, ConsumerC<Channel> listener) {
        return write(bootstrap, payload, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture writer(Bootstrap bootstrap, Object payload, Consumer2<Throwable> onErr) {
        return write(bootstrap, payload, THROW_CHANNEL_AWAY, onErr);
    }

    public static ChannelFuture write(Bootstrap bootstrap, final Object payload, final ConsumerC<Channel> listener, final Consumer2<Throwable> onErr) {
        return connect(bootstrap, new ConsumerC<Channel>() {
            @Override
            public void accept(final Channel channel) throws Exception {
                write(channel, payload, new ActionC() {
                    @Override
                    public void exec() throws Exception {
                        listener.accept(channel);
                    }
                }, new Consumer2<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (ReferenceCountUtil.refCnt(payload) > 0)
                            ReferenceCountUtil.release(payload);
                        onErr.accept(throwable);
                    }
                });
            }
        }, new Consumer2<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                if (ReferenceCountUtil.refCnt(payload) > 0)
                    ReferenceCountUtil.release(payload);
                onErr.accept(throwable);
            }
        });
    }


    public static ChannelFuture connect(Bootstrap bootstrap) {
        return connect(bootstrap, THROW_CHANNEL_AWAY, THROW_LOG_AWAY);
    }

    public static ChannelFuture connect(Bootstrap bootstrap, ConsumerC<Channel> listener) {
        return connect(bootstrap, listener, THROW_LOG_AWAY);
    }

    public static ChannelFuture connecter(Bootstrap bootstrap, Consumer2<Throwable> onErr) {
        return connect(bootstrap, THROW_CHANNEL_AWAY, onErr);
    }

    public static ChannelFuture connect(Bootstrap bootstrap, final ConsumerC<Channel> listener, final Consumer2<Throwable> onErr) {
        if (listener == null)
            throw new NullPointerException("listener (onConnect)");
        if (onErr == null)
            throw new NullPointerException("listener (onConnectError)");

        ChannelFuture cf = null;
        try {
            cf = bootstrap.connect();
            final ChannelFuture channelFuture = cf;
            cf.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> f) throws Exception {
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

    public static boolean close(final ChannelHandlerContext ctx, Object payload) {
        write(ctx, payload, new ActionC() {
            @Override
            public void exec() throws Exception {
                ctx.close();
            }
        }, new Consumer2<Throwable>() {
            @Override
            public void accept(Throwable err) {
                logger.warn("could not write final payload to channel", err);
            }
        });
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

