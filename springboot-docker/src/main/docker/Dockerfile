# Docker image for Springboot file run
# Version 1.0
# Author: root
FROM openjdk:8u131-jre-alpine
VOLUME /tmp
COPY target/springboot-docker.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar ${0} ${@}"]