package com.example.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.VaadinServletContextInitializer;

public class RouteInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

    private ApplicationContext context;

    public RouteInitializationAotProcessor(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(
            ConfigurableListableBeanFactory beanFactory) {
        VaadinServletContextInitializer vsci = new VaadinServletContextInitializer(context);
        List<Class<?>> routeClasses = vsci.findByAnnotation(
                vsci.getRoutePackages(), Route.class, RouteAlias.class)
                .collect(Collectors.toList());

        return (generationContext, beanFactoryInitializationCode) -> {
            for (Class<?> routeClass : routeClasses) {
                System.out.println("Route class: "+routeClass.getName());
                generationContext.getRuntimeHints().reflection().registerType(routeClass,
                        MemberCategory.values());
            }
        };
    }

}
