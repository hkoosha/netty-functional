package cc.koosha.nettyfunctional.nettyfunctions;

import cc.koosha.nettyfunctional.checkedfunction.FunctionC;


public interface Matcher extends FunctionC<Object, Boolean> {

    @Override
    Boolean apply(Object object);

}
