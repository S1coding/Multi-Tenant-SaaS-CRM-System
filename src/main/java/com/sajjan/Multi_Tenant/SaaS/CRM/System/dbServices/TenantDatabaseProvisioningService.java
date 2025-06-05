package com.sajjan.Multi_Tenant.SaaS.CRM.System.dbServices;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Service
public class TenantDatabaseProvisioningService {

	@Value("${spring.datasource.url}")
	private  String adminUrl; //use annotation to copy from application.properties
	@Value("${spring.datasource.username}")
	private String adminUser; //use annotation to copy from application.properties
	@Value("${spring.datasource.password}")
	private String adminPassword; //use annotation to copy from application.properties

	public void createDatabaseAndRunFlyway(String dbName) throws Exception {
		// Step 1: Connect to admin DB (usually 'postgres')
		try (Connection conn = DriverManager.getConnection(adminUrl, adminUser, adminPassword);
		     Statement stmt = conn.createStatement()) {

			// Step 2: Create the new database
			stmt.executeUpdate("CREATE DATABASE " + dbName);
		} catch (Exception e){
			throw new Exception("Could not connect to database");
		}

		// Step 3: Run Flyway migrations on the new database
		String hostAndPort = adminUrl.substring("jdbc:postgresql://".length(), adminUrl.lastIndexOf("/"));
		String tenantUrl = "jdbc:postgresql://" + hostAndPort + "/" + dbName; //in fx() parameters, schema has to be pre-existing


		Flyway flyway = Flyway.configure() //configures, datasource with tenant url
				.dataSource(tenantUrl, adminUser, adminPassword)
				.locations("classpath:db/migration")
				.load();

		flyway.migrate();
	}
}
