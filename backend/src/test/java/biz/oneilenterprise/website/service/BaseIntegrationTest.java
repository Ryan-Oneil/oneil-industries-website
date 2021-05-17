package biz.oneilenterprise.website.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/testData.sql"})
@Sql(scripts = "/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class BaseIntegrationTest {

}
