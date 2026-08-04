FROM ubuntu:latest

RUN apt update && \
    apt upgrade -y && \
    apt install jdk-17-jdk -y && \
    apt install maven -y

WORKDIR /opt/app

COPY . .

RUN mvn clean package

CMD ["java", "-jar", "target/app.jar"]