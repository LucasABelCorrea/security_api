FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /opt/app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine-3.23     
WORKDIR /opt/app
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar
# Envio de um valor padrão para minha variável de ambiente
# ENV SPRING_PROFILES_ACTIVE=dev
# CMD [ "java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar" ]
CMD [ "java", "-jar", "app.jar" ]