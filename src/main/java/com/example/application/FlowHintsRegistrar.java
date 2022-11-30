package com.example.application;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.internal.JavaScriptBootstrapUI;
import com.vaadin.flow.router.InternalServerError;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.internal.DefaultErrorHandler;
import org.springframework.aot.hint.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FlowHintsRegistrar implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        ReflectionHints ref = hints.reflection();
        ResourceHints res = hints.resources();
        try {
            for (Class<?> c : getFlowClasses()) {
                ref.registerType(c, MemberCategory.values());
            }
            for(String c : getFlowPrivateClasses()){
                ref.registerType(TypeReference.of(c), MemberCategory.values());
            }
            for(String r : getFlowResourcePatterns()) {
                hints.resources().registerPattern(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<? extends Class<?>> getFlowClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JavaScriptBootstrapUI.class);
        classes.add(InternalServerError.class);
        classes.add(RouteNotFoundError.class);
        classes.add(JavaScriptBootstrapUI.ClientViewPlaceholder.class);
        classes.add(DefaultErrorHandler.class);
        classes.add(MainLayout.class);
        classes.add(ClickEvent.class);
        classes.add(KeyDownEvent.class);
        return classes;
    }

    private Collection<String> getFlowPrivateClasses() {
        Set<String> classes = new HashSet<>();
        classes.add("com.vaadin.flow.router.RouteNotFoundError.LazyInit.class");
        return classes;
    }

    private Collection<String> getFlowResourcePatterns() {
        Set<String> patterns = new HashSet<>();
        patterns.add("*RouteNotFoundError_dev.html");
        patterns.add("*RouteNotFoundError_prod.html");
        return patterns;
    }
}
