package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.checkedfunction.ConsumerC;
import cc.koosha.nettyfunctional.nettyfunctions.Write;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.SocketAddress;


public enum NettyFunc {
    ;

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


    public static final ConsumerC<Channel> NOOP = new ConsumerC<Channel>() {
        @Override
        public void accept(final Channel channel) throws Exception {

        }
    };

}

