package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface BiConsumerC<T, U> {

    void accept(T t, U u) throws Exception;

}
