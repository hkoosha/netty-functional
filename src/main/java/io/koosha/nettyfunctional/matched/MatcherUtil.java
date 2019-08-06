package io.koosha.nettyfunctional.matched;

import io.koosha.nettyfunctional.nettyfunctions.Matcher;
import io.netty.util.ReferenceCountUtil;

import java.util.Objects;


public enum MatcherUtil {

    ;

    public static Matcher classMatcher(final Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz (type)");
        return new Matcher() {
            @Override
            public Boolean apply(Object object) {
                return clazz.isAssignableFrom(object.getClass());
            }
        };
    }

    static void release(Object object) {
        if (ReferenceCountUtil.refCnt(object) > 0)
            ReferenceCountUtil.release(object);
    }

}
