package uk.co.ashleyfrieze.jdbijooqdaoframework.dao;

import org.jdbi.v3.core.mapper.RowMapper;
import uk.co.ashleyfrieze.jdbijooqdaoframework.context.DaoContext;

import java.util.List;
import java.util.Optional;

/**
 * Common implementation of any DAO
 * @param <T> type of POJO used to map database data
 * @param <I> identifier type
 */
public class BaseDaoImpl<T, I> implements BaseDao<T, I> {
    private DaoContext context;
    private RowMapper<T> defaultMapper;
    private String tableName;
    private String idField;

    public BaseDaoImpl(DaoContext context, RowMapper<T> defaultMapper, String tableName, String idField) {
        this.context = context;
        this.defaultMapper = defaultMapper;
        this.tableName = tableName;
        this.idField = idField;
    }

    @Override
    public List<T> getAll() {
        return context.getJdbi().withHandle(handle ->
            handle.createQuery("SELECT * FROM " + tableName)
                    .map(defaultMapper)
                    .list());
    }

    @Override
    public Optional<T> get(I id) {
        return context.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM " + tableName + " WHERE " + idField + " = :id")
                    .bind("id", id)
                    .map(defaultMapper)
                    .findFirst());
    }
}
