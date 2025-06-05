package com.sajjan.Multi_Tenant.SaaS.CRM.System.security;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.security.filters.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private TenantDetailsService tenantDetailsService;

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Value("${accepted.client.one}")
	private String clientAddress;
	@Value("${accepted.client.two}")
	private String clientAddressTwo;

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors((cors) -> cors
						.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// Tenant only endpoints
						.requestMatchers("/registerTenantOrg").permitAll()
						.requestMatchers("/tenant/login").permitAll()
						.requestMatchers("/tenant/register").permitAll()
						.requestMatchers("/tenant/generateJwtToken").permitAll()
						.requestMatchers("/tenant/updateTenantDetails").permitAll()
						.requestMatchers("/tenant/getTenantsByName").hasAnyAuthority("ADMIN", "MANAGER")

						// Admin only endpoints
						.requestMatchers("/tenant/promoteTenant").hasAnyAuthority("ADMIN")
						.requestMatchers("/contact/updateContact").hasAuthority("ADMIN")
						.requestMatchers("/contact/getContactsByEmail").hasAuthority("ADMIN")

						// Contact module access control
						//TODO: remove all where any authority can access it, its pointless
						.requestMatchers("/contact/getAllContacts").hasAnyAuthority("ADMIN")
						.requestMatchers("/contact/updateContactForTenant").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/contact/addContactForTenant").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/contact/getContactsForTenant").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/contact/deleteContactForTenant").hasAnyAuthority("USER", "ADMIN", "MANAGER")

						// Deals endpoints
						.requestMatchers("/deal/createDeal").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/deal/updateDeal").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/deal/getMyDeals").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/deal/deleteDealMadeTo").hasAnyAuthority("USER", "ADMIN", "MANAGER")

						// Tasks endpoints
						.requestMatchers("/task/createNewTask").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/task/editTask").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/task/getPendingTasksByDueDate").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/task/getLastUpdatedTask").hasAnyAuthority("USER", "ADMIN", "MANAGER")
						.requestMatchers("/task/markTaskAsComplete").hasAnyAuthority("USER", "ADMIN", "MANAGER")


						//Report endpoints
						.requestMatchers("/report/getReportByProjectName").hasAnyAuthority("ADMIN", "MANAGER")
						.requestMatchers("/report/makeReport").hasAnyAuthority("ADMIN", "MANAGER")
						.requestMatchers("/report/getReportPdf").hasAnyAuthority("ADMIN", "MANAGER")
						.requestMatchers("/report/getMyReports").hasAnyAuthority("ADMIN", "MANAGER")

						//TEST TENANT:
						//{
						//	"email": "alex.morgan@example.com",
							//	"password": "StrongPass!2025"
						//}

						// Everything else requires authentication
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	@Bean
	public AuthenticationManager authenticationManager(){
		System.out.println("entered authenticationManager");
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(tenantDetailsService);
		return new ProviderManager(provider);
	}

	@Bean
	UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(clientAddress, clientAddressTwo));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
