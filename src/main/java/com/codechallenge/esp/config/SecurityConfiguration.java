package com.codechallenge.esp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private static final String ROLES = "roles";
	private static final String REALM_ACCESS = "realm_access";

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
        		.requestMatchers("/unauthenticated", "/login/**", "/").permitAll()
        		.anyRequest().authenticated())
                .oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
                    .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
                    	.userAuthoritiesMapper(grantedAuthoritiesMapper())
                    )
            	);
        
        return http.build();
    }
    
    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (OidcUserAuthority.class.isInstance(authority)) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;
                    
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
                    Map<String, Object> realmAccess = userInfo.getClaim(REALM_ACCESS);
                    
                    Collection<String> realmRoles;
                    if (null != realmAccess && 
                    		(realmRoles = (Collection<String>) realmAccess.get(ROLES)) != null) {
                    	
                        realmRoles.forEach(role -> 
                        	mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                    }
                }
            });
            return mappedAuthorities;
        };
    }   
}
