package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.verification.verficationtoken;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.Tenants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private VerificationTokenService tokenService;

	@Autowired
	private EmailService emailService;

	@Value("live.server.url")
	private String serverUrl;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		Tenants tenant = event.getUser();
		String token = UUID.randomUUID().toString();
		tokenService.createVerificationToken(tenant, token);

		String recipientAddress = tenant.getEmail();
		String subject = "Email Verification";
		String confirmationUrl = serverUrl+"verify-email?token=" + token;
		String message = "Click the link to verify your email: " + confirmationUrl;

		emailService.sendEmail(recipientAddress, subject, message);
	}
}