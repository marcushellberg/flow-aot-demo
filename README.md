# Flow + Spring Boot 3 native example

The native support in Spring Boot 3 puts certain requirements on the project. The most relevant for Flow applications is related to Java reflection usage, where you need to compile time define for which classes reflection information should be available and which resources should be available from the classpath.

In this proof of concept, this is implemented as part of the application.

## Prerequisite

You need GraalVM 22.3+. Get it [from the source (graalvm.org)](https://www.graalvm.org/downloads/) or use e.g. sdkman.
Ensure it is set as the value to your `JAVA_HOME` environment variable. 

## Compiling the native app

To compile a native binary, run
```

./mvnw -Pproduction -Pnative spring-javaformat:apply clean package native:compile
```

You can then start the native application as
```
target/flow-native
```

## Should the code really be in my application project?

No, this is a proof of concept. All the GraalVM native image and Spring Boot 3 AOT-related code here should end up in Vaadin Flow or the Atmosphere project.
