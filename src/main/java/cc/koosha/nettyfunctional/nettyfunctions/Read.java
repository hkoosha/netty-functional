package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.BiConsumerC;
import io.netty.channel.ChannelHandlerContext;


@FunctionalInterface
public interface Read<T> extends BiConsumerC<ChannelHandlerContext, T> {

    @Override
    void accept(ChannelHandlerContext channelHandlerContext, T o)
            throws Exception;

}
