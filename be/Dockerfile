FROM ibm-semeru-runtimes:open-11-jre
VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE
ARG ACTIVE_PROFILE
COPY ${JAR_FILE} app.jar
ENV ACT_PROFILE_ENV ${ACTIVE_PROFILE}
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=${ACT_PROFILE_ENV} -jar /app.jar
