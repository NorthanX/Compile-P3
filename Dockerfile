FROM openjdk:13
WORKDIR /app/
COPY ./* ./
RUN javac opg.java
