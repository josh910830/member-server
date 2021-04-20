FROM openjdk:11-jre

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY target/member-server-*.jar member-server.jar

ENTRYPOINT ["java", "-jar", "member-server.jar"]
CMD ["--spring.profiles.active=prod"]
