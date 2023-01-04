#!/usr/bin/env bash
./mvnw -Pproduction -Pnative spring-javaformat:apply clean package native:compile && ./target/flow-native