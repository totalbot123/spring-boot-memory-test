FROM eclipse-temurin:17-jre-alpine

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-XX:MinHeapFreeRatio=20","-XX:MaxHeapFreeRatio=20","-XX:InitialRAMPercentage=90","-XX:MaxRAMPercentage=90","-jar","/app.jar"]
