package io.koosha.nettyfunctional.nettyfunctions;

import io.koosha.nettyfunctional.checkedfunction.TiConsumerC;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


@FunctionalInterface
public interface Write<T> extends TiConsumerC<ChannelHandlerContext, T, ChannelPromise> {

    @Override
    void accept(ChannelHandlerContext channelHandlerContext, T t, ChannelPromise promise)
            throws Exception;

}
