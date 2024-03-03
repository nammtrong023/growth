# FROM eclipse-temurin:17.0.5_8-jre-focal as builder
# WORKDIR /extracted
# ADD ./target/*.jar app.jar
# RUN java -Djarmode=layertools -jar app.jar extract

# FROM eclipse-temurin:17.0.5_8-jre-focal
# WORKDIR /application
# COPY --from=builder extracted/dependencies/ ./
# COPY --from=builder extracted/snapshot-dependencies/ ./
# COPY --from=builder extracted/spring-boot-loader/ ./
# COPY --from=builder extracted/application/ ./

# ENTRYPOINT ["java" , "org.springframework.boot.loader.launch.JarLauncher"]

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]