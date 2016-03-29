package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.FunctionC;
import lombok.NonNull;


@FunctionalInterface
public interface Matcher extends FunctionC<Object, Boolean> {

    @Override
    Boolean apply(Object object);


    static Matcher classMatcher(@NonNull final Class<?> clazz) {

        return object -> clazz.isAssignableFrom(object.getClass());
    }

}
