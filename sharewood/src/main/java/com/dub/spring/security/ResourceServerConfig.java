package com.dub.spring.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**
* ResourceServerConfiguration used by Spring OAuth2 
* */

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(
		  prePostEnabled = true) 
		  //securedEnabled = true, 
		  //jsr250Enabled = true)
public class ResourceServerConfig
								extends ResourceServerConfigurerAdapter {

	@Value("${security.oauth2.resource.id}")
	private String resourceId;
	
	
	//JWT
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //JWT
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    //JWT    
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        
        Resource resource = new ClassPathResource("public.txt");
             
        try {
			InputStream is = resource.getInputStream();
			String publicKey = getStringFromInputStream(is);

			converter.setVerifierKey(publicKey);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return converter;
    }
    
   
	@Override
	public void configure(HttpSecurity http) 
								throws Exception {		
		
	    http.httpBasic().disable();
	
		http.antMatcher("/api/**").authorizeRequests()	
		.antMatchers(HttpMethod.GET, "/api/testResource")
		.access("#oauth2.hasScope('TRUST')")
			
		.antMatchers(HttpMethod.GET, "/api/photos/**")
		.access("#oauth2.hasScope('TRUST') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.POST, "/api/photos/**")
		.access("#oauth2.hasScope('TRUST') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.PUT, "/api/photos/**")
		.access("#oauth2.hasScope('TRUST') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.DELETE, "/api/photos/**")
		.access("#oauth2.hasScope('TRUST') and hasAuthority('USER')");
				
	}


	@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
				
		resources.resourceId(resourceId);
		resources.accessDeniedHandler(new OAuth2AccessDeniedHandler());
		resources.expressionHandler(new OAuth2WebSecurityExpressionHandler());
		
		resources.tokenServices(tokenServices());
	}
	
	
	// helper static method that converts InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	
}