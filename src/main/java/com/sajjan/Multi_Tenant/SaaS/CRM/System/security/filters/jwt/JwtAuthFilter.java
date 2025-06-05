package com.sajjan.Multi_Tenant.SaaS.CRM.System.security.filters.jwt;

import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetails;
import com.sajjan.Multi_Tenant.SaaS.CRM.System.entities.tenant.tenantDetails.TenantDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private TenantDetailsService tenantDetailsService;

//	@Autowired
//	public JwtAuthFilter(TenantDetailsService tenantDetailsService){
//		this.tenantDetailsService = tenantDetailsService;
//	}

	private Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getServletPath();

		if (path.equals("/tenant/generateJwtToken") ||
				path.equals("/tenant/register") ||
				path.equals("/tenant/login") ||
				path.equals("/registerTenantOrg")) {

			logger.info("Skipping JwtAuthFilter for public endpoint: {}", path);
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");
		String token = null;

		if(authHeader != null && authHeader.startsWith("Bearer ")){
			token = authHeader.substring(7);
		}

		if(token!=null && JwtUtil.validateToken(token)){
			String username = JwtUtil.extractUsername(token);

			TenantDetails tenantDetails = (TenantDetails) tenantDetailsService.loadUserByUsername(username);
			Authentication auth = new UsernamePasswordAuthenticationToken(username, null, tenantDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			logger.info("jwt token {} retrieved in jwtAuthFilter", token);
		}else{
			logger.info("invalid token {} in jwtAuthFilter", token);
		}

		filterChain.doFilter(request, response);
	}
}
