package training.aws.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.jdbc.datasource.AbstractDataSource;
import software.amazon.awssdk.services.rds.RdsUtilities;

public class RdsIamDataSource extends AbstractDataSource {

  private final RdsUtilities rdsUtilities;
  private final String hostname;
  private final int port;
  private final String username;
  private final String jdbcUrl;
  private final String sslRootCert;

  public RdsIamDataSource(
      RdsUtilities rdsUtilities,
      String hostname,
      int port,
      String database,
      String username,
      String sslRootCert) {

    this.rdsUtilities = rdsUtilities;
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.sslRootCert = sslRootCert;

    this.jdbcUrl = String.format(
        "jdbc:postgresql://%s:%d/%s",
        hostname, port, database
    );
  }

  @Override
  public Connection getConnection() throws SQLException {
    return createConnection(username);
  }

  @Override
  public Connection getConnection(
      String username, String password) throws SQLException {
    return createConnection(username);
  }

  private Connection createConnection(String username)
      throws SQLException {

    String token = rdsUtilities.generateAuthenticationToken(
        builder -> builder
            .hostname(hostname)
            .port(port)
            .username(username)
    );

    Properties properties = new Properties();

    properties.setProperty("user", username);
    properties.setProperty("password", token);
    properties.setProperty("sslmode", "verify-full");
    properties.setProperty("sslrootcert", sslRootCert);

    return DriverManager.getConnection(jdbcUrl, properties);
  }
}
