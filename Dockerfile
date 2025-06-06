FROM gradle:8.6-jdk21-alpine AS build
WORKDIR /app

# Копируем только основные файлы сборки
COPY build.gradle settings.gradle ./

# Скачиваем зависимости
RUN gradle dependencies --no-daemon

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN gradle clean build --no-daemon -x test

# Финальный образ
FROM eclipse-temurin:21.0.2_13-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]