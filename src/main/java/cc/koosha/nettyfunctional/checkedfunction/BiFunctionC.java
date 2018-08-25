package cc.koosha.nettyfunctional.checkedfunction;


public interface BiFunctionC<T, U, R> {

        R apply(T t, U u) throws Exception;

}
