package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.BiFunctionC;
import io.netty.channel.ChannelHandlerContext;


@FunctionalInterface
public interface ReadTransformer<T> extends BiFunctionC<ChannelHandlerContext, T, Object> {

    @Override
    Object apply(ChannelHandlerContext ctx, T msg) throws Exception;

}
