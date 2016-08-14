package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.TiFunctionC;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


public interface WriteTransformer<T> extends TiFunctionC<ChannelHandlerContext, T, ChannelPromise, Object> {

    @Override
    Object apply(ChannelHandlerContext channelHandlerContext, T msg, ChannelPromise promise)
            throws Exception;

}
