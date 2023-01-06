package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.annotation.Processor;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.AtmosphereAnnotation;
import org.atmosphere.config.managed.ManagedServiceInterceptor;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.*;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.SuspendTrackerInterceptor;
import org.atmosphere.util.AbstractBroadcasterProxy;
import org.atmosphere.util.ExcludeSessionBroadcaster;
import org.atmosphere.util.SimpleBroadcaster;
import org.reflections.Reflections;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

			var reflections = new Reflections("org.atmosphere");

			var all = new HashSet<>(Set.of(Push.class, AtmosphereHandlerService.class, AbstractBroadcasterProxy.class,
					DefaultBroadcaster.class, AsyncSupportListener.class, AtmosphereFrameworkListener.class,
					ExcludeSessionBroadcaster.class, SimpleBroadcaster.class, AtmosphereResourceEventListener.class,
					AtmosphereInterceptor.class, BroadcastFilter.class, AtmosphereResource.class,
					AtmosphereResourceImpl.class, AtmosphereResourceLifecycleInterceptor.class,
					TrackMessageSizeInterceptor.class, SuspendTrackerInterceptor.class,
					ManagedServiceInterceptor.class));
			all.addAll(getProcessorClasses(reflections));
			for (var c : all)
				hints.reflection().registerType(c, mcs);

			for (var c : getGraalVmJavaAgentClasses())
				hints.reflection().registerType(c, mcs);

			for (var r : Set.of("/META-INF/services/org.atmosphere.inject.Injectable",
					"/org/atmosphere/util/version.properties"
			// "/org/atmosphere/cpr/AtmosphereFramework.class",
			/*
			 * "/org/atmosphere/cpr/AtmosphereHandler.class",
			 * "/org/atmosphere/cpr/AtmosphereResourceEventListener.class",
			 * "/org/atmosphere/cpr/AtmosphereResourceEventListenerAdapter.class",
			 * "/org/atmosphere/cpr/AtmosphereResourceHeartbeatEventListener.class",
			 * "/org/atmosphere/cpr/AtmosphereServletProcessor.class",
			 * "/org/atmosphere/cpr/AbstractReflectorAtmosphereHandler.class",
			 * "org.atmosphere.handler.AbstractReflectorAtmosphereHandler"
			 */))
				hints.resources().registerResource(new ClassPathResource(r));

		}

		private static Collection<TypeReference> getGraalVmJavaAgentClasses() {
			var trs = Set.of("org.atmosphere.spring.SpringWebObjectFactory", "org.atmosphere.cdi.CDIObjectFactory",
					"org.atmosphere.kafka.KafkaBroadcaster", "org.atmosphere.plugin.hazelcast.HazelcastBroadcaster",
					"org.atmosphere.plugin.jgroups.JGroupsBroadcaster", "org.atmosphere.plugin.jms.JMSBroadcaster",
					"org.atmosphere.plugin.rabbitmq.RabbitMQBroadcaster",
					"org.atmosphere.plugin.redis.RedisBroadcaster", "org.atmosphere.plugin.rmi.RMIBroadcaster",
					"org.atmosphere.plugin.xmpp.XMPPBroadcaster", "org.atmosphere.guice.GuiceObjectFactory").stream()
					.map(TypeReference::of).toList();
			var clazzes = Set.of(org.atmosphere.cache.UUIDBroadcasterCache.class,
					org.atmosphere.container.JSR356AsyncSupport.class, org.atmosphere.container.JSR356Endpoint.class,
					org.atmosphere.cpr.AtmosphereFramework.class, org.atmosphere.cpr.AtmosphereInterceptorAdapter.class,
					org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter.class,
					org.atmosphere.cpr.AtmosphereResourceImpl.class,
					org.atmosphere.cpr.DefaultAtmosphereResourceFactory.class,
					org.atmosphere.cpr.DefaultAtmosphereResourceSessionFactory.class,
					org.atmosphere.cpr.DefaultBroadcaster.class, org.atmosphere.cpr.DefaultBroadcasterFactory.class,
					org.atmosphere.cpr.DefaultMetaBroadcaster.class,
					org.atmosphere.handler.AbstractReflectorAtmosphereHandler.class,
					org.atmosphere.inject.AtmosphereConfigInjectable.class,
					org.atmosphere.inject.AtmosphereFrameworkInjectable.class,
					org.atmosphere.inject.AtmosphereResourceFactoryInjectable.class,
					org.atmosphere.inject.AtmosphereResourceSessionFactoryInjectable.class,
					org.atmosphere.inject.BroadcasterFactoryInjectable.class,
					org.atmosphere.inject.InjectIntrospectorAdapter.class,
					org.atmosphere.inject.MetaBroadcasterInjectable.class,
					org.atmosphere.inject.PostConstructIntrospector.class,
					org.atmosphere.inject.WebSocketFactoryInjectable.class,
					org.atmosphere.interceptor.AndroidAtmosphereInterceptor.class,
					org.atmosphere.interceptor.CacheHeadersInterceptor.class,
					org.atmosphere.interceptor.CorsInterceptor.class,
					org.atmosphere.interceptor.IdleResourceInterceptor.class,
					org.atmosphere.interceptor.JSONPAtmosphereInterceptor.class,
					org.atmosphere.interceptor.JavaScriptProtocol.class,
					org.atmosphere.interceptor.OnDisconnectInterceptor.class,
					org.atmosphere.interceptor.PaddingAtmosphereInterceptor.class,
					org.atmosphere.interceptor.SSEAtmosphereInterceptor.class,
					org.atmosphere.interceptor.WebSocketMessageSuspendInterceptor.class,
					org.atmosphere.util.VoidAnnotationProcessor.class,
					org.atmosphere.websocket.protocol.SimpleHttpProtocol.class

			);
			var al = new ArrayList<TypeReference>();
			al.addAll(clazzes.stream().map(TypeReference::of).toList());
			al.addAll(trs);
			return al;
		}

		private static Collection<Class<?>> getProcessorClasses(Reflections reflections) {

			var types = new HashSet<Class<?>>();
			reflections.getTypesAnnotatedWith(AtmosphereAnnotation.class).forEach(c -> {
				types.add(c);
				types.add(c.getAnnotation(AtmosphereAnnotation.class).value());
			});
			types.addAll(reflections.getSubTypesOf(Processor.class));
			return types;
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
