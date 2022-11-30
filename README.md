# Flow + Spring Boot 3 native example

The native support in Spring Boot 3 puts certain requirements on the project. The most relevant for Flow applications is related to Java reflection usage, where you need to compile time define for which classes reflection information should be available and which resources should be available from the classpath.

In this proof of concept, this is implemented as part of the application.

## Prerequisite

You need GraalVM 22.3+. Get it from https://www.graalvm.org/downloads/ or use e.g. sdkman.
Ensure it is set as your JAVA_HOME

## Compiling the native app

To compile a native binary, run
```
./mvnw -Pproduction -Pnative native:compile
```

You can then start the native application as
```
target/hilla-native
```


## Should the code really be in my application project?

No, this is a proof of concept. All the native related code here should end up in Flow and Atmosphere.