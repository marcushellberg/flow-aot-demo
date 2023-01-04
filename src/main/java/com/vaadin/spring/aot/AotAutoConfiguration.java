package com.vaadin.spring.aot;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

@AutoConfiguration
@ImportRuntimeHints({ AtmosphereHintsRegistrar.class })
class AotAutoConfiguration {

	@Bean
	static FlowBeanFactoryInitializationAotProcessor flowBeanFactoryInitializationAotProcessor() {
		return new FlowBeanFactoryInitializationAotProcessor();
	}

}
