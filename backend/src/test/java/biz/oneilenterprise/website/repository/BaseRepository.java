package biz.oneilenterprise.website.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql({"/testData.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class BaseRepository {

    @Autowired
    protected TestEntityManager entityManager;

    @AfterEach
    public void cleanup() {
        entityManager.clear();
    }

}
