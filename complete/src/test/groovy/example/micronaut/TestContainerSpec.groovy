package example.micronaut

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.sql.ResultSet
import java.sql.Statement

@Testcontainers
class TestContainerSpec extends Specification {

    void 'sample run where you get a direct connection'() {
        when:
        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
                .withDatabaseName("foo")
                .withUsername("foo")
                .withPassword("secret")
        postgreSQLContainer.start()

        println postgreSQLContainer.getJdbcUrl()
        HikariConfig hikariConfig = new HikariConfig()
        hikariConfig.setJdbcUrl(postgreSQLContainer.jdbcUrl)
        hikariConfig.setUsername("foo")
        hikariConfig.setPassword("secret")
        HikariDataSource ds = new HikariDataSource(hikariConfig)

        Statement statement = ds.getConnection().createStatement()
        statement.execute("SELECT 1")
        ResultSet resultSet = statement.getResultSet()
        resultSet.next()

        then: "result is returned"
        int resultSetInt = resultSet.getInt(1)
        resultSetInt == 1
         1 == 1
    }

}
