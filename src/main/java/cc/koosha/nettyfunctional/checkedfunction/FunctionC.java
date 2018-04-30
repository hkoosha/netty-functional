package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface FunctionC<T, R> {

    R apply(T t) throws Exception;

}
