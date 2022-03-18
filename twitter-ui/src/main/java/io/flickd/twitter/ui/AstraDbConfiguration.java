package io.flickd.twitter.ui;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;

@Configuration
@Profile("astra")
@EnableCassandraRepositories(basePackages = "io.flickd.twitter.ui.repository")
public class AstraDbConfiguration extends AbstractCassandraConfiguration {

	@Value("${astra-db.bundle}")
	private String secureConnectBundlePath;
	
	@Value("${astra-db.username}")
	private String username;
	
	@Value("${astra-db.password}")
	private String password;
	
	@Value("${astra-db.keyspace}")
	private String keyspaceName;

	@Override
	protected String getKeyspaceName() {
		
		return "twitter";
	}
	
	@Override
	  public SchemaAction getSchemaAction() {
	    return SchemaAction.CREATE_IF_NOT_EXISTS;
	  }


	@Override
	public String[] getEntityBasePackages() {
	    return new String[]{"io.flickd.twitter.model"};
	}
	
    @Override
    protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
        return new SessionBuilderConfigurer() {
            @Override
            public CqlSessionBuilder configure(CqlSessionBuilder cqlSessionBuilder) {
                    try {
						return cqlSessionBuilder
						        .withCloudSecureConnectBundle(Paths.get(new FileSystemResource(secureConnectBundlePath).getURI()))
//						        .withCloudSecureConnectBundle(Paths.get(new ClassPathResource(secureConnectBundlePath).getURI()))
						        .withAuthCredentials(username, password)
						        .withKeyspace(keyspaceName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return cqlSessionBuilder;
            }
        };
    }

}