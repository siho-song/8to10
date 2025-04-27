#!/bin/sh

JAR_FILE=$(ls /app/build/*.jar 2>/dev/null | head -n 1)

if [ -n "$JAR_FILE" ]; then
  echo "Running JAR file: $JAR_FILE with profile: dev"
  java -Dspring.profiles.active=dev -Dfile.encoding=UTF-8 -jar "$JAR_FILE"
else
  echo "No JAR file found in /app/build"
  exit 1
fi
