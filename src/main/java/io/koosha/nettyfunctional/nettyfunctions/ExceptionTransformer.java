package io.koosha.nettyfunctional.nettyfunctions;

import io.netty.channel.ChannelHandlerContext;


@FunctionalInterface
public interface ExceptionTransformer<T extends Throwable> {

    Throwable apply(ChannelHandlerContext channelHandlerContext, T exception)
            throws Exception;

}
