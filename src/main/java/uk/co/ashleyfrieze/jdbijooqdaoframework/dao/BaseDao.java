package uk.co.ashleyfrieze.jdbijooqdaoframework.dao;

import java.util.List;
import java.util.Optional;

/**
 * The common parts of all DAOs
 * @param <T> type of POJO used for representing data
 * @param <I> the type of the ID field in the pojo
 */
public interface BaseDao<T, I> {
    /**
     * Retrieve all objects
     * @return all objects
     */
    List<T> getAll();

    /**
     * Retrieve one object by id
     * @param id the id of the object
     * @return the first found object or {@link Optional#empty} if not found
     */
    Optional<T> get(I id);
}
