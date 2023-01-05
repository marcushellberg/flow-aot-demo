package com.example.application;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.stream.Stream;

@EnableWebSecurity
@Configuration
class SecurityConfiguration extends VaadinWebSecurity {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		setLoginView(http, LoginView.class);
	}

	@Bean
	UserDetailsManager userDetailsService() {
		var list = Stream.of("josh", "marcus")
				.map(u -> User.withUsername(u).password("{noop}" + u).roles("USER").build()).toList();
		return new InMemoryUserDetailsManager(list);
	}

}