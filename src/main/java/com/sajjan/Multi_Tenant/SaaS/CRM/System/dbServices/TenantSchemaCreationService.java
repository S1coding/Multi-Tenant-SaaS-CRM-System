package com.sajjan.Multi_Tenant.SaaS.CRM.System.dbServices;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class TenantSchemaProvisioningService {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;

	//not used
	@Value("${spring.datasource.url}")
	private String baseUrl;

	//not used
	@Value("${spring.datasource.username}")
	private String dbUser;

	//not used
	@Value("${spring.datasource.password}")
	private String dbPassword;


	//parameterized
	public TenantSchemaProvisioningService(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
	}

	//only run when admin account created
	public void createSchemaAndRunMigrations(String schemaName) {
		// Validate schema name to prevent SQL injection
		if (!schemaName.matches("^[a-zA-Z0-9_]+$")) {
			throw new IllegalArgumentException("Invalid schema name");
		}

		// Create the schema
		jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);

		// Configure Flyway for the new schema
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.schemas(schemaName)
				.locations("classpath:db/migration/tenant") //TODO: is this valid?
				.load();

		flyway.migrate();
	}
}