package uk.co.ashleyfrieze.jdbijooqdaoframework.mapper;

import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

/**
 * Most field binding can be done by linking the right column name with the right results set
 * manipulation and property setter on the target object
 * @param <T> POJO representing the DB data
 * @param <V> value type of the given field within the POJO
 */
public class PropertyBinding<T, V> implements FieldBinding<T> {
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
    static <T, V> PropertyBinding<T, V> propertyBinding(BiConsumer<T, V> fieldSetter,
                                                               ResultsSetReader<V> resultSetReader,
                                                               String columnName) {
        return new PropertyBinding<>(fieldSetter, set -> resultSetReader.read(set, columnName));
    }

    @Override
    public void apply(T target, ResultSet rs, StatementContext ctx) throws SQLException {
        fieldSetter.accept(target, resultSetGetter.apply(rs));
    }
}
