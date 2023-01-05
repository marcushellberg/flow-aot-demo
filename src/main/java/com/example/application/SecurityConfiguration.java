package com.example.application;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.apache.catalina.core.ApplicationContextFacade;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@EnableWebSecurity
@Configuration
@ImportRuntimeHints(SecurityConfiguration.MyHints.class)
class SecurityConfiguration extends VaadinWebSecurity {

	static class MyHints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			for (var c : new String[] {
					"org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler$SupplierCsrfToken",
					ApplicationContextFacade.class.getName(), com.vaadin.flow.component.login.LoginI18n.class.getName(),
					com.vaadin.flow.component.login.LoginI18n.Form.class.getName(),
					com.vaadin.flow.component.login.LoginI18n.ErrorMessage.class.getName() }) {
				hints.reflection().registerType(TypeReference.of(c), MemberCategory.values());
			}

			hints.resources().registerResource(new ClassPathResource("com/vaadin/flow/component/login/i18n.json"));
		}

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		setLoginView(http, LoginView.class);
	}

	@Bean
	UserDetailsManager userDetailsService() {
		var josh = User.withUsername("josh").password("{noop}josh").roles("USER").build();
		var marcus = User.withUsername("marcus").password("{noop}marcus").roles("USER").build();

		return new InMemoryUserDetailsManager(josh, marcus);
	}

}