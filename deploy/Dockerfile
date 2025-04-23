FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/*.jar ./app.jar
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    chown -R appuser:appuser /app
USER appuser
CMD ["java", "-jar", "app.jar"]