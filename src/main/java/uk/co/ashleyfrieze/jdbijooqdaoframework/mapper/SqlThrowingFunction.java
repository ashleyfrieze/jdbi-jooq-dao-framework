package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import java.sql.SQLException;

/**
 * Internal: when the {@link ResultsSetReader} is curried to
 * a simpler function-like interface, this allows that function to throw
 * @param <T> type of input object
 * @param <R> return type
 */
@FunctionalInterface
public interface SqlThrowingFunction<T, R> {
    R apply(T t) throws SQLException;
}
