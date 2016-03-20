@echo off
java -jar transfer-report-ui-0.0.1-SNAPSHOT.war --spring.config.location=ENV/%1/application.properties
