package uk.co.ashleyfrieze.daoimpl;

import org.jdbi.v3.core.Jdbi;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.SelectWhereStep;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.Test;
import uk.co.ashleyfrieze.jdbijooqdaoframework.context.DaoContext;
import uk.co.ashleyfrieze.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class UserDaoImplTest {
    private static final String DB_CONNECTION = "jdbc:h2:mem:testdb;MODE=MSSQLServer;INIT=RUNSCRIPT FROM './src/test/resources/create.sql'";

    @Test
    public void jooqWorksButIsLimited() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);

        try (DSLContext create = DSL.using(SQLDialect.SQLITE)) {
            SelectWhereStep<?> select = create
                    .select(field("*"))
                    .from(table("User"));

            String sql = select.getSQL(ParamType.NAMED_OR_INLINED);

            System.out.println(sql);

            List<User> map = jdbi.withHandle(handle -> {
                return handle.createQuery(sql)
                        .map(UserDaoImpl.USER_ROW_MAPPER)
                        .list();
            });

            System.out.println(map);
        }
    }

    @Test
    public void baseDao() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        System.out.println(dao.getAll());
    }

    @Test
    public void selectOne() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        User billy = new User();
        billy.setId(1L);
        billy.setName("Billy");

        assertThat(dao.get(1L)).hasValue(billy);
    }

    @Test
    public void selectOneWhenMissing() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        assertThat(dao.get(999L)).isEmpty();
    }
}