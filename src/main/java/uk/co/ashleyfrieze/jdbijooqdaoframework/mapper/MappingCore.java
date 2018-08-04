package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Reusable implementation for {@link RowMapper} to avoid dropping to reflection.
 */
public class MappingCore {
    /**
     * Allows one of the {@link ResultSet} read functions to be chosen by a mapper
     * @param <V> the return value of reading the result set at the given column
     */
    @FunctionalInterface
    public interface ResultsSetReader<V> {
        V read(ResultSet set, String col) throws SQLException;
    }

    /**
     * Internal: when the {@link MappingCore.ResultsSetReader} is curried to
     * a simpler function-like interface, this allows that function to throw
     * @param <T> type of input object
     * @param <R> return type
     */
    @FunctionalInterface
    public interface SqlThrowingFunction<T, R> {
        R apply(T t) throws SQLException;
    }

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

    /**
     * Most field binding can be done by linking the right column name with the right results set
     * manipulation and property setter on the target object
     * @param <T> POJO representing the DB data
     * @param <V> value type of the given field within the POJO
     */
    public static class PropertyBinding<T, V> implements FieldBinding<T> {
        private BiConsumer<T, V> fieldSetter;
        private SqlThrowingFunction<ResultSet, V> resultSetGetter;

        private PropertyBinding(BiConsumer<T, V> fieldSetter, SqlThrowingFunction<ResultSet, V> resultSetGetter) {
            this.fieldSetter = fieldSetter;
            this.resultSetGetter = resultSetGetter;
        }

        /**
         * Create a property binding - this takes user-convenient inputs and makes a property binding object
         * @param fieldSetter the method on the pojo to call to set the field - e.g. Pojo::setSomething
         *                    can also be a lambda, e.g. <code>(object, value) -> object.doSomethingWith(value);</code>
         * @param resultSetReader function that will read a value from a results set and return something of type V
         * @param columnName the name of the column to read from the results set
         * @param <T> type of POJO representing the DB data
         * @param <V> type of value in the column/field
         * @return a property binding
         */
        public static <T, V> PropertyBinding<T, V> propertyBinding(BiConsumer<T, V> fieldSetter,
                                                                   ResultsSetReader<V> resultSetReader,
                                                                   String columnName) {
            return new PropertyBinding<>(fieldSetter, set -> resultSetReader.read(set, columnName));
        }

        @Override
        public void apply(T target, ResultSet rs, StatementContext ctx) throws SQLException {
            fieldSetter.accept(target, resultSetGetter.apply(rs));
        }
    }

    /**
     * Creates a mapper from an object creator and some bindings
     * @param emptyObject supplies a fresh object to write to
     * @param bindings binds fields from the results set into the target object
     * @param <T> type of object mapped
     * @return a new {@link RowMapper}
     */
    public static <T> RowMapper<T> createMapper(Supplier<T> emptyObject, List<FieldBinding<T>> bindings) {
        return (rs, ctx) -> {
            T newObject = emptyObject.get();

            // for loop as the apply method throws
            for(FieldBinding<T> binding:bindings) {
                binding.apply(newObject, rs, ctx);
            }

            return newObject;
        };
    }

}
