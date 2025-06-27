package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.verification.verficationtoken;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private final Tenants tenant;

	public OnRegistrationCompleteEvent(Tenants user) {
		super(user);
		this.tenant = user;
	}

	public Tenants getUser() {
		return tenant;
	}
}
