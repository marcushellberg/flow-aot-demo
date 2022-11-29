package com.example.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.container.JSR356AsyncSupport;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.DefaultAtmosphereResourceFactory;
import org.atmosphere.cpr.DefaultAtmosphereResourceSessionFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.cpr.DefaultBroadcasterFactory;
import org.atmosphere.cpr.DefaultMetaBroadcaster;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;
import org.atmosphere.util.SimpleBroadcaster;
import org.atmosphere.util.VoidAnnotationProcessor;
import org.atmosphere.websocket.protocol.SimpleHttpProtocol;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class AtmosphereHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        ReflectionHints ref = hints.reflection();
        try {
            for (Class<?> c : getAtmosphereClasses()) {
                ref.registerType(c, MemberCategory.values());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Collection<? extends Class<?>> getAtmosphereClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DefaultAtmosphereResourceFactory.class);
        classes.add(SimpleHttpProtocol.class);

        classes.addAll(AtmosphereFramework.DEFAULT_ATMOSPHERE_INTERCEPTORS);
        classes.add(AtmosphereResourceLifecycleInterceptor.class);
        classes.add(TrackMessageSizeInterceptor.class);
        classes.add(SuspendTrackerInterceptor.class);
        classes.add(DefaultBroadcasterFactory.class);
        classes.add(SimpleBroadcaster.class);
        classes.add(DefaultBroadcaster.class);
        classes.add(UUIDBroadcasterCache.class);
        classes.add(VoidAnnotationProcessor.class);
        classes.add(DefaultAtmosphereResourceSessionFactory.class);
        classes.add(JSR356AsyncSupport.class);
        classes.add(DefaultMetaBroadcaster.class);

        return classes;
    }
}