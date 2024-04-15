FROM tomcat:11.0.0-M17
LABEL maintainer="kimia.abedini@studenti.unipd.it"

# Add the tomcat users to the docker compose
ADD ./src/main/resources/tomcat-users.xml /usr/local/tomcat/conf/

# Change the context to be accessible from the outside network
ADD ./src/main/resources/context.xml /tmp/context.xml

# modify the and add changes
RUN mv /usr/local/tomcat/webapps /usr/local/tomcat/webapps2
RUN mv /usr/local/tomcat/webapps.dist /usr/local/tomcat/webapps
RUN cp /tmp/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml

# Add the war file of the project
ADD ./target/cycleK-1.0.0.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]