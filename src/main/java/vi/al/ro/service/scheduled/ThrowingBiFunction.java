package vi.al.ro.service.scheduled;

/**
 * Like {@link java.util.function.BiFunction} but with Exception
 * @param <T> first argument
 * @param <U> second argument
 * @param <R> result
 * @param <X> throwable class
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, X extends Exception> {

    R apply(T t, U u) throws X;
}
