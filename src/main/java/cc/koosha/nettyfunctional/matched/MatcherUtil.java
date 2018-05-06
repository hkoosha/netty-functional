package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.util.ReferenceCountUtil;

import java.util.Objects;


public enum MatcherUtil {

    ;

    public static Matcher classMatcher(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz (type)");
        return object -> clazz.isAssignableFrom(object.getClass());
    }

    static void release(Object object) {
        if (ReferenceCountUtil.refCnt(object) > 0)
            ReferenceCountUtil.release(object);
    }

}
