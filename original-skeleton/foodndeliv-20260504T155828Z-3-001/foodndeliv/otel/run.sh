#!/bin/bash

#wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

java -javaagent:./opentelemetry-javaagent.jar \
     -Dotel.traces.exporter=otlp -Dotel.metrics.exporter=none -Dotel.logs.exporter=none \
     -Dotel.exporter.otlp.endpoint="http://localhost:4317" \
     -Dotel.exporter.otlp.traces.protocol=grpc \
     -Dotel.service.name=foodndeliv \
     -Dotel.traces.sampler=always_on \
     -jar foodndeliv-0.0.1-SNAPSHOT.jar