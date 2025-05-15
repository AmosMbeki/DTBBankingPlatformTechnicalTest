FROM openjdk:21
LABEL author="amosnyagar <amosmbeki@gmail.com>"
WORKDIR /customer-service
ADD target/customer-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "customer-service-0.0.1-SNAPSHOT.jar"]