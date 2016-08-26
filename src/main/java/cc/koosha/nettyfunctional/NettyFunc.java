package cc.koosha.nettyfunctional;

import cc.koosha.nettyfunctional.checkedfunction.ConsumerC;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import lombok.NonNull;


public enum NettyFunc {
    ;

    public static ChannelHandler initer(@NonNull final ConsumerC<Channel> accept) {

        return new NettyFunc.InitChannel(accept);

    }

    @Sharable
    private static final class InitChannel extends ChannelInitializer<Channel> {

        private final ConsumerC<Channel> apply;

        public InitChannel(@NonNull final ConsumerC<Channel> apply) {
            this.apply = apply;
        }

        protected void initChannel(final Channel ch) throws Exception {

            this.apply.accept(ch);
        }

    }

    public static final ConsumerC<Channel> NOOP = new ConsumerC<Channel>() {
        @Override
        public void accept(final Channel channel) throws Exception {

        }
    };

    public static final ChannelHandler NOOP_CHANNEL_INIT = initer(NOOP);

}

