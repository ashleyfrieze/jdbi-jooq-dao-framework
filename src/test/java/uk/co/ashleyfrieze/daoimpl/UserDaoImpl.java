package uk.co.ashleyfrieze.daoimpl;

import org.jdbi.v3.core.mapper.RowMapper;
import uk.co.ashleyfrieze.dao.UserDao;
import uk.co.ashleyfrieze.jdbijooqdaoframework.context.DaoContext;
import uk.co.ashleyfrieze.jdbijooqdaoframework.dao.BaseDaoImpl;
import uk.co.ashleyfrieze.model.User;

import java.sql.ResultSet;

import static java.util.Arrays.asList;
import static uk.co.ashleyfrieze.jdbijooqdaoframework.mapper.MappingCore.PropertyBinding.propertyBinding;
import static uk.co.ashleyfrieze.jdbijooqdaoframework.mapper.MappingCore.createMapper;

public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {
    public static final RowMapper<User> USER_ROW_MAPPER = createMapper(
                    User::new,
                    asList(
                            propertyBinding(User::setId, ResultSet::getLong, "id"),
                            propertyBinding(User::setName, ResultSet::getString, "name"))
                    );

    public UserDaoImpl(DaoContext context) {
        super(context, USER_ROW_MAPPER, "User", "id");
    }
}
