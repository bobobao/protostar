package io.github.yezhihao.protostar.util.lambda;

import java.util.function.Function;

/**
 * @author baoqingyun
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R accept(T t) throws E;

    static <T, R> Function<T, R> throwingFunctionWrapper(ThrowingFunction<T, R, Exception> throwingFunction) {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
