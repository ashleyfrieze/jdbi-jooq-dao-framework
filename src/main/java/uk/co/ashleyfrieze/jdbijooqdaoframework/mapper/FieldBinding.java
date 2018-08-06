package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines how a single column to field mapping works
 * @param <T> type of POJO representing the db data
 */
public interface FieldBinding<T> {
    /**
     * Apply the contents of the results set/ctx to the target object
     * @param target to write
     * @param rs results set
     * @param ctx statement context
     * @throws SQLException on db-related error
     */
    void apply(T target, ResultSet rs, StatementContext ctx) throws SQLException;
}
