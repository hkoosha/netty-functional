package io.koosha.nettyfunctional.checkedfunction;


public interface BiConsumerC<T, U> {

    void accept(T t, U u) throws Exception;

}
