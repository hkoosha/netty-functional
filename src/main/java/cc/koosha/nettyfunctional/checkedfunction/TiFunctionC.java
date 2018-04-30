package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface TiFunctionC <T, U, V, R> {

        R apply(T t, U u, V v) throws Exception;

}
