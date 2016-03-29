package cc.koosha.nettyfunctional.depr;

import io.netty.channel.ChannelHandlerContext;


@Deprecated
public interface IfRead<T> {

    boolean read(ChannelHandlerContext ctx, T msg);

}
