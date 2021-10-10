FROM adoptopenjdk/openjdk11:latest
COPY target/product-service-*.jar product-service.jar
ENTRYPOINT ["java","-jar","/product-service.jar"]