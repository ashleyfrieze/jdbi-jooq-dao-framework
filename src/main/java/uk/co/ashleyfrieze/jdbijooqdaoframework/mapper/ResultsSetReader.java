package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Allows one of the {@link ResultSet} read functions to be chosen by a mapper
 * @param <V> the return value of reading the result set at the given column
 */
@FunctionalInterface
public interface ResultsSetReader<V> {
    V read(ResultSet set, String col) throws SQLException;
}
