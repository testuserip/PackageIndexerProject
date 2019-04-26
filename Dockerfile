#Download base image ubuntu latest
FROM ubuntu:latest
 
# Update Software repository
RUN apt-get update
RUN apt-cache search openjdk

# Install OpenJDK-11
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

#Install git
RUN apt-get update \
    && apt-get install -y git
 
#Change directory and clone Qxf2 Public POM repo
RUN mkdir /usr/testuserip \
    && cd /usr/testuserip \
    && git clone https://github.com/radhikabanhatti/PackageIndexer.git

# Expose the port that server listens on
EXPOSE 8080

# Cd into the executable directory and run tests
RUN cd /usr/testuserip/PackageIndexer \
    && java -jar PackageIndexer.jar
