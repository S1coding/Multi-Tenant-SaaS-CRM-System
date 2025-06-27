package com.sajjan.Multi_Tenant.SaaS.CRM.System.services.verification.verficationtoken;

import java.security.SecureRandom;

public class RandomStringGenerator {

	private static String CHARACTERS =  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom random = new SecureRandom();

	public static String generateString(int length){
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++){
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return sb.toString();
	}
}
