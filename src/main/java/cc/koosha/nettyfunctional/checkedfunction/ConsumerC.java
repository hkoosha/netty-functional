package cc.koosha.nettyfunctional.checkedfunction;


public interface ConsumerC<T> {

    void accept(T t) throws Exception;

}
