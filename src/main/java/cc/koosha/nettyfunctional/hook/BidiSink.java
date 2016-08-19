package cc.koosha.nettyfunctional.hook;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;


public abstract class BidiSink<I, O> extends BidiTransform<I, O> {

    protected BidiSink() {
    }

    protected BidiSink(@NonNull final Class<?> iType,
                       @NonNull final Class<?> oType) {

        super(iType, oType);
    }

    protected BidiSink(@NonNull final Matcher iMatcher,
                       @NonNull final Matcher oMatcher) {

        super(iMatcher, oMatcher);
    }

    @Override
    protected final Object read1(final ChannelHandlerContext ctx,
                                 final I msg) throws Exception {

        this.read2(ctx, msg);
        return null;
    }

    @Override
    protected final Object write1(final ChannelHandlerContext ctx,
                                  final O msg,
                                  final ChannelPromise promise) throws Exception {

        this.write2(ctx, msg, promise);
        return null;
    }


    protected abstract void write2(final ChannelHandlerContext ctx,
                                   final O msg,
                                   final ChannelPromise promise) throws Exception;

    protected abstract void read2(final ChannelHandlerContext ctx, final I msg) throws Exception;

}
