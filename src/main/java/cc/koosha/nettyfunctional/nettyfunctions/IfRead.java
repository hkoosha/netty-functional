package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.BiFunctionC;
import io.netty.channel.ChannelHandlerContext;


@FunctionalInterface
public interface IfRead<T> extends BiFunctionC<ChannelHandlerContext, T, Boolean> {

    @Override
    Boolean apply(ChannelHandlerContext channelHandlerContext, T t) throws Exception;

}
