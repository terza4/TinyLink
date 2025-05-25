#!/bin/sh

echo "🕓 Waiting for MariaDB to be ready..."
while ! nc -z db 3306; do
  sleep 1
done

echo "✅ MariaDB is ready, starting the app"
java -jar app.jar
