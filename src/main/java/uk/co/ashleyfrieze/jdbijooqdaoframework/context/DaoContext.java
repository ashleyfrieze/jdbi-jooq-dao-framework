package uk.co.ashleyfrieze.jdbijooqdaoframework.context;

import org.jdbi.v3.core.Jdbi;

public class DaoContext {
    private Jdbi jdbi;

    public DaoContext(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}
