package com.example.application;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.VaadinServletContextInitializer;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class RouteInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

	private final ApplicationContext context;

	public RouteInitializationAotProcessor(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
		VaadinServletContextInitializer vsci = new VaadinServletContextInitializer(context);
		List<Class<?>> routeClasses = vsci.findByAnnotation(vsci.getRoutePackages(), Route.class, RouteAlias.class)
				.toList();

		return (generationContext, beanFactoryInitializationCode) -> {
			for (Class<?> routeClass : routeClasses) {
				generationContext.getRuntimeHints().reflection().registerType(routeClass, MemberCategory.values());
				generationContext.getRuntimeHints().resources().registerType(routeClass);
			}
		};
	}

}
