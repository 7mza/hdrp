## Handwritten Digit Recognition Project, M1 WI 2015/16  

Standalone webapp with Jetty/H2  
Learning data in /src/main/resources/data/data.sql  

#### Team
BAAZIZ Hamza  
AMYAR Amine  
BENDARI Yassi  

#### Requirements
JDK >= 7  
Maven >= 3  

#### How to run ?
mvn clean package  
mvn jetty:run  
then http://localhost:8080/index.html  

#### To check Database
CD to {HOME_DIRECTORY}/.m2/repository/com/h2database/h2/1.4.191  
then java -jar h2-1.4.191.jar 

####
En cas d'erreur utiliser la commande mvn package puis deployer le .war generer sur un serveur tomcat
