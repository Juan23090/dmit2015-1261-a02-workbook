package dmit2015.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * In-memory H2 data source used only by the Arquillian test deployment.
 */
@DataSourceDefinition(
        name = "java:app/datasources/TestH2DatabaseDS",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:campus-event-test;DB_CLOSE_DELAY=-1",
        user = "user2015",
        password = "Password2015")
@ApplicationScoped
public class TestDataSourceConfig {

}
