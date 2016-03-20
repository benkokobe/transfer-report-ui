[[ $1 == "" ]] && echo "please provide as argument the source environment:" && exit 1

env=$(echo $1 | tr 'A-Z' 'a-z')
java -jar transfer-report-ui-0.0.1-SNAPSHOT.war --spring.config.location=ENV/$env/application.properties
