package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.managed.ManagedServiceInterceptor;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.*;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;
import org.atmosphere.util.AbstractBroadcasterProxy;
import org.atmosphere.util.ExcludeSessionBroadcaster;
import org.atmosphere.util.SimpleBroadcaster;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.Set;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation to make the application installable on phones, tablets and some
 * desktop browsers.
 */
@Push
@SpringBootApplication
@Theme(variant = Lumo.DARK)
@ImportRuntimeHints(Application.Hints.class)
public class Application implements AppShellConfigurator {

	static class Hints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			var mcs = MemberCategory.values();

			for (var c : Set.of(AtmosphereHandlerService.class, AbstractBroadcasterProxy.class,
					DefaultBroadcaster.class, AsyncSupportListener.class, AtmosphereFrameworkListener.class,
					ExcludeSessionBroadcaster.class, SimpleBroadcaster.class, AtmosphereResourceEventListener.class,
					AtmosphereInterceptor.class, BroadcastFilter.class, AtmosphereResource.class,
					AtmosphereResourceImpl.class, AtmosphereResourceLifecycleInterceptor.class,
					TrackMessageSizeInterceptor.class, SuspendTrackerInterceptor.class,
					ManagedServiceInterceptor.class))
				hints.reflection().registerType(c, mcs);
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
