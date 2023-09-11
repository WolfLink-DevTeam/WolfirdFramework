FROM alpine:latest

ENV MINECRAFT_VERSION=1.20.1
ENV PAPER_VERSION=173
ENV PLUGIN_VERSION=1.2.9
RUN mkdir /app \
&& apk update \
&& apk add sudo \
&& apk add openjdk17-jre \
&& wget -P /app https://api.papermc.io/v2/projects/paper/versions/$MINECRAFT_VERSION/builds/$PAPER_VERSION/downloads/paper-$MINECRAFT_VERSION-$PAPER_VERSION.jar \
&& echo eula=true > /app/eula.txt
COPY ./target/wolfird-framework-$PLUGIN_VERSION.jar /app/plugins/WolfirdFramework-$PLUGIN_VERSION.jar
WORKDIR /app
CMD sudo java -server -jar paper-$MINECRAFT_VERSION-$PAPER_VERSION.jar