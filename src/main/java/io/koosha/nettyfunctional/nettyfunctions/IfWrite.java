package io.koosha.nettyfunctional.nettyfunctions;

import io.koosha.nettyfunctional.checkedfunction.TiFunctionC;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


@FunctionalInterface
public interface IfWrite<T> extends TiFunctionC<ChannelHandlerContext, T, ChannelPromise, Boolean> {

    @Override
    Boolean apply(ChannelHandlerContext channelHandlerContext, T t, ChannelPromise promise) throws Exception;

}
