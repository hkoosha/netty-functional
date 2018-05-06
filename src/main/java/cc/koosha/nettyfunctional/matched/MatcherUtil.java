package cc.koosha.nettyfunctional.matched;

import cc.koosha.nettyfunctional.nettyfunctions.Matcher;
import lombok.NonNull;


public enum  MatcherUtil {

    ;

    public static Matcher classMatcher(@NonNull final Class<?> clazz) {
        return object -> clazz.isAssignableFrom(object.getClass());
    }

}
