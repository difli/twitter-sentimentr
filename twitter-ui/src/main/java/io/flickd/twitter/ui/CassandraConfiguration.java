package io.flickd.twitter.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@Profile("!astra")
@EnableCassandraRepositories(basePackages = "io.flickd.twitter.ui.repository")
public class CassandraConfiguration extends AbstractCassandraConfiguration {

	@Value("${cassandra.contact-points}")
	String contactPoints;
	
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
	protected String getContactPoints() {
		return contactPoints;
	}
}