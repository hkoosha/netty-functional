package io.koosha.nettyfunctional.checkedfunction;


public interface TiFunctionC <T, U, V, R> {

        R apply(T t, U u, V v) throws Exception;

}
