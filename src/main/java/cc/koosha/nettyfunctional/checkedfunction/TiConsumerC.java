package cc.koosha.nettyfunctional.checkedfunction;


@FunctionalInterface
public interface TiConsumerC<T, U, V> {

    void accept(T t, U u, V v) throws Exception;

    default TiConsumerC<T, U, V> andThen(final TiConsumerC<? super T, ? super U, ? super V> after) {

        if(after == null)
            return this;

        return (l, r, v) -> {
            accept(l, r, v);
            after.accept(l, r, v);
        };
    }

}
