package io.koosha.nettyfunctional.checkedfunction;


public interface TiConsumerC<T, U, V> {

    void accept(T t, U u, V v) throws Exception;

}
