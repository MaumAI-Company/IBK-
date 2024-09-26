# Use the official Tomcat image from the Docker Hub
FROM tomcat:8.5.59-jdk11

RUN mkdir /ibk_file

# Remove the default web applications
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy the WAR file into the Tomcat webapps directory
COPY ibk-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# # Expose port 8080
EXPOSE 18081
