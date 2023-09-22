FROM amazoncorretto:17.0.8
WORKDIR /app
COPY build/libs/payment-gateway.jar /app/payment-gateway.jar
EXPOSE 8080
CMD ["java", "-jar", "payment-gateway.jar"]