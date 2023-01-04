package com.example.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets and some
 * desktop browsers.
 */
@SpringBootApplication
@Theme(value = "flow-native")
@PWA(name = "flow-native", shortName = "flow-native", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@NpmPackage(value = "@vaadin-component-factory/vcf-nav", version = "1.0.6")
@ImportRuntimeHints({ AtmosphereHintsRegistrar.class })
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	static FlowBeanFactoryInitializationAotProcessor flowBeanFactoryInitializationAotProcessor() {
		return new FlowBeanFactoryInitializationAotProcessor();
	}

	@Bean
	static RouteInitializationAotProcessor routeInitializationAotProcessor(ApplicationContext context) {
		return new RouteInitializationAotProcessor(context);
	}

}
