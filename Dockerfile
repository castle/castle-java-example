# Fetch the Castle browser SDK from npm and build the Tailwind stylesheet.
FROM node:20-slim AS frontend
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY tailwind.config.js ./
COPY src/tailwind.css ./src/tailwind.css
COPY src/main/resources/templates ./src/main/resources/templates
COPY static ./static
RUN npm run build:css

# Build the Spring Boot jar.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

# Final runtime image.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/castle-example.jar ./app.jar
COPY --from=frontend /app/static ./static
COPY --from=frontend /app/node_modules/@castleio/castle-js/dist ./node_modules/@castleio/castle-js/dist

ENV location=docker
ENV PORT=80

# Only the Castle credentials are needed at runtime (e.g. docker run -e ...);
# the simulated demo user values are baked in as code defaults.

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar"]
