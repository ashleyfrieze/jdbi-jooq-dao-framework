package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Reusable implementation for {@link RowMapper} to avoid dropping to reflection.
 */
public final class Mapping {
    /**
     * For ease of mapping, provide default out-of-the-box results set reading functions
     */
    public static final ResultsSetReader<Long> LONG_COLUMN = ResultSet::getLong;
    public static final ResultsSetReader<Integer> INT_COLUMN = ResultSet::getInt;
    public static final ResultsSetReader<Date> DATE_COLUMN = ResultSet::getDate;
    public static final ResultsSetReader<String> STRING_COLUMN = ResultSet::getString;

    private Mapping() {
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
        return PropertyBinding.propertyBinding(fieldSetter, resultSetReader, columnName);
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
