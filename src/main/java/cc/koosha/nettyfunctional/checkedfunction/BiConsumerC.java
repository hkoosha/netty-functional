package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface BiConsumerC<T, U> {

    void accept(T t, U u) throws Exception;

    default BiConsumerC<T, U> andThen(final BiConsumerC<? super T, ? super U> after) {

        if(after == null)
            return this;

        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }

}
