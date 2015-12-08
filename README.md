# Introduction #

This is a small web application which tries to help RIC team members with the generation of release content during the execution and after release is executed.

During the execution of a release it's very important for the person who is executing the release to have a global view of the content of the release without having to consult manually diffeerent DB tables for the content of the release.

A given release has the following content:

* List of patches
* DB members
* Transfer operations

### DR related excel sheet generator: ###

The menu "DR generator" used to generate an Excel sheet with three tabs that consists patch list, DB members and the transfer operations.

### Report formatter: ###

This menu is to format the report generated by the "Advanced report" of Change and adds different columns such as Synopsis of the patch, if a group is activated or not. This functionality will be further developped soon.

### How it works: ###
Unzip the file report_generator.zip found in the link below:

\\sufp0001\data\PROJETS\RIC\X04 Team Members Directory\BKO\

cd report_generator
generate.sh <env> ( e.g. MNY)

This will fire-up the application which is embedded in a tomcat application server. The application is accessed  http://localhost:8080

enter your DR and click submit, and the excel file should automatically be generated and downloaded to your default download location. The name of the generated excel file is the same as the entered DR name.

### How do I make it work for a specific project ###

The application.properties file in each ENV/<Project> directories will be like the one shown below:

#MoneyYou
env.name=MoneyYou
spring.datasource.url=jdbc:oracle:thin:@localhost:8417:DENGPRG
spring.datasource.username=cw
spring.datasource.password=cw
spring.datasource.driverClassName=oracle.jdbc.driver.OracleDriver

