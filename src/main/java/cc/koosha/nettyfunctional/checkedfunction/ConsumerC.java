package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface ConsumerC<T> {

    void accept(T t) throws Exception;

}
