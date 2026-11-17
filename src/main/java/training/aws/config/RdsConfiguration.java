package training.aws.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsUtilities;

@Configuration
@Profile("aws")
public class RdsConfiguration {

  @Bean
  public RdsUtilities rdsUtilities(
      @Value("${aws.region}") String region) {

    return RdsUtilities.builder()
        .region(Region.of(region))
        .credentialsProvider(
            DefaultCredentialsProvider.create()
        )
        .build();
  }

  @Bean
  public DataSource dataSource(
      RdsUtilities rdsUtilities,
      @Value("${aws.rds.hostname}") String hostname,
      @Value("${aws.rds.port}") int port,
      @Value("${aws.rds.database}") String database,
      @Value("${aws.rds.username}") String username,
      @Value("${aws.rds.ssl-root-cert}") String sslRootCert) {

    RdsIamDataSource dataSource = new RdsIamDataSource(
        rdsUtilities,
        hostname,
        port,
        database,
        username,
        sslRootCert
    );

    HikariConfig config = new HikariConfig();

    config.setDataSource(dataSource);
    config.setPoolName("RdsIamPool");
    config.setMaximumPoolSize(5);
    config.setMinimumIdle(1);

    return new HikariDataSource(config);
  }
}