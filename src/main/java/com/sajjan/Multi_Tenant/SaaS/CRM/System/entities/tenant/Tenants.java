package com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Tenant {

	String id;
	String firstName;
	String lastName;
	String email;
	int phoneNumber;
	String authority;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
