FROM openjdk:8-alpine
RUN mkdir -p /opt/BuchLadenTester
WORKDIR /opt/BuchLadenTester
COPY target/BuchLadenTester-0.0.1-SNAPSHOT.jar /opt/BuchLadenTester
ENV JAVA_ARGS=""
CMD java $JAVA_ARGS -jar BuchLadenTester-0.0.1-SNAPSHOT.jar
EXPOSE 8080