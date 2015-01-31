package com.rspl.hackit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@SuppressWarnings("serial")
	private Map<String, String> users = new HashMap<String, String>() {
		{
			put("team-1", "rspl123#");
			put("team-2", "rspl123#");
			put("team-3", "rspl123#");
			put("team-4", "rspl123#");
			put("team-5", "rspl123#");
		}
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/login").failureUrl("/login?error")
				.permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll().and().headers()
				.disable();
		http.csrf().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		for (Entry<String, String> entry : users.entrySet()) {
			auth.inMemoryAuthentication().withUser(entry.getKey())
					.password(entry.getValue()).roles("USER");
		}

	}

}
