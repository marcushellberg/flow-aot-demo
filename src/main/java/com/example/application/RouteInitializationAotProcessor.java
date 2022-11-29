package com.example.application;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.router.Route;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


class RouteInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(
            ConfigurableListableBeanFactory beanFactory) {
        String[] routes = beanFactory.getBeanNamesForAnnotation(Route.class);
        Set<Class<?>> routeClasses = new HashSet<>();

        for (String route : routes) {
            routeClasses.add(beanFactory.getType(route));
        }

        return (generationContext, beanFactoryInitializationCode) -> {
            for (Class<?> routeClass : routeClasses) {
                generationContext.getRuntimeHints().reflection().registerType(routeClass,
                        MemberCategory.values());
            }
        };
    }
}