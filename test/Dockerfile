# Stage 1
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --continue

# Stage 2
FROM amazoncorretto:17-alpine3.20

WORKDIR /app
COPY --from=builder /app/build/libs/test-0.0.1-SNAPSHOT.jar .

RUN apk update && \
    apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/America/Guayaquil /etc/localtime && \
    echo "America/Guayaquil" > /etc/timezone

EXPOSE 8080

LABEL version="1.0" \
      description="Test Application" \
      maintainer="Mauricio Ruiz <m4dicio@gmail.com>"

CMD ["java", "-jar", "test-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]