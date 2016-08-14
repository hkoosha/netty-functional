package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import lombok.NonNull;


public enum  MatcherUtil {

    ;

    public static Matcher classMatcher(@NonNull final Class<?> clazz) {

        return new Matcher() {
            @Override
            public Boolean apply(final Object object) {
                return clazz.isAssignableFrom(object.getClass());
            }
        };
    }

}
