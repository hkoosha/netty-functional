package cc.koosha.nettyfunctional.checkedfunction;


public interface FunctionC<T, R> {

    R apply(T t) throws Exception;

}
