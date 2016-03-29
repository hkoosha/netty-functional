package cc.koosha.nettyfunctional.depr;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


@Deprecated
public interface IfWrite<T> {

    boolean write(ChannelHandlerContext ctx, T msg, ChannelPromise promise);

}
