package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface BiFunctionC<T, U, R> {

        R apply(T t, U u) throws Exception;

}
