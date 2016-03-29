package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface ConsumerC<T> {

    void accept(T t) throws Exception;

    default ConsumerC<T> andThen(final ConsumerC<? super T> after) {

        if(after == null)
            return this;

        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

}
