FROM openjdk:21
LABEL author="amosnyagar <amosmbeki@gmail.com>"
WORKDIR /card-service
ADD target/card-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "card-service-0.0.1-SNAPSHOT.jar"]