package dmit2015.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Defines the JTA data source referenced by the `<jta-data-source>` element in `persistence.xml`.
 * <p>
 * The H2 database file is stored outside the project folder (in ~/jdk/databases/h2)
 * so the database file is never committed to the repository.
 */
@DataSourceDefinitions({

        @DataSourceDefinition(
                name = "java:app/datasources/H2DatabaseDS",
                className = "org.h2.jdbcx.JdbcDataSource",
                url = "jdbc:h2:file:~/jdk/databases/h2/DMIT2015-1261-A3-JuanRodriguez-DB",
                user = "user2015",
                password = "Password2015"),

})

@ApplicationScoped
public class ApplicationConfig {

}
