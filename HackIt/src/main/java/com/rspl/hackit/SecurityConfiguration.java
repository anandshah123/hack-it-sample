package com.rspl.hackit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
			put("team-1", "d2afd019");
			put("team-2", "d78411d6");
			put("team-3", "16b6ec92");
			put("team-4", "36dadebf");
			put("team-5", "eb75062a");
			put("team-6", "912e23ea");
			put("team-7", "e8d08d2e");
			// put("team-8", "ad116e4b");
			// put("team-9", "e84e2bc3");
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
		http.sessionManagement().maximumSessions(1);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		for (Entry<String, String> entry : users.entrySet()) {
			auth.inMemoryAuthentication().withUser(entry.getKey())
					.password(entry.getValue()).roles("USER");
		}

	}

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
