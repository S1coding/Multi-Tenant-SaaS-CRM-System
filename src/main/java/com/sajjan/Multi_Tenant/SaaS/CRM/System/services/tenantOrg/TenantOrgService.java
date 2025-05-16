package com.sajjan.Multi_Tenant.SaaS.CRM.System.services;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TenantOrgService {
	@Autowired
	private DataSource dataSource;

	public void createSchemaForTenant(String schemaName) {
		try (Connection connection = dataSource.getConnection()) {
			connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create schema: " + schemaName, e);
		}
	}
}
