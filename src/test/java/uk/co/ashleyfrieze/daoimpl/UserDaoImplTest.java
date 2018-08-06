package uk.co.ashleyfrieze.daoimpl;

import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import uk.co.ashleyfrieze.jdbijooqdaoframework.context.DaoContext;
import uk.co.ashleyfrieze.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoImplTest {
    private static final String DB_CONNECTION = "jdbc:h2:mem:testdb;MODE=MSSQLServer;INIT=RUNSCRIPT FROM './src/test/resources/create.sql'";

    @Test
    public void baseDao_getAll() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        List<User> users = dao.getAll();

        assertThat(users).hasSize(10);
        assertThat(users.get(0).getName()).isEqualTo("Billy");
    }

    @Test
    public void selectOne_withExistingData_findsData() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        User billy = new User();
        billy.setId(1L);
        billy.setName("Billy");

        assertThat(dao.get(1L)).hasValue(billy);
    }

    @Test
    public void selectOne_whenNoAppropriateData_isEmpty() {
        Jdbi jdbi = Jdbi.create(DB_CONNECTION);
        UserDaoImpl dao = new UserDaoImpl(new DaoContext(jdbi));

        assertThat(dao.get(999L)).isEmpty();
    }
}