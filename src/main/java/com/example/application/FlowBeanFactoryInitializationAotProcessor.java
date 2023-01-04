package com.example.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.internal.DefaultErrorHandler;
import org.reflections.Reflections;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class FlowBeanFactoryInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

	@Override
	public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
		return (generationContext, beanFactoryInitializationCode) -> {
			var hints = generationContext.getRuntimeHints();
			var memberCategories = MemberCategory.values();
			for (var pkg : getPackages(beanFactory)) {

				var reflections = new Reflections(pkg);

				for (var c : reflections.getSubTypesOf(Component.class))
					hints.reflection().registerType(c, memberCategories);

				for (var c : reflections.getSubTypesOf(HasErrorParameter.class))
					hints.reflection().registerType(c, memberCategories);

				for (var c : reflections.getSubTypesOf(ComponentEvent.class))
					hints.reflection().registerType(c, memberCategories);

				for (var c : Set.of("com.vaadin.flow.router.RouteNotFoundError.LazyInit.class",
						DefaultErrorHandler.class.getName()))
					hints.reflection().registerType(TypeReference.of(c), memberCategories);

				for (String r : Set.of("*RouteNotFoundError_dev.html", "*RouteNotFoundError_prod.html"))
					hints.resources().registerPattern(r);
			}
		};
	}

	private static List<String> getPackages(BeanFactory beanFactory) {
		var listOf = new ArrayList<String>();
		listOf.add("com.vaadin");
		listOf.addAll(AutoConfigurationPackages.get(beanFactory));
		return listOf;
	}

}
