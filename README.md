Work Log
=============================

01-Jan-2022  
Added OPEN API Swagger dependencies & Configurations added in the security configurations for URLs.  
http://localhost:8080/swagger-ui/index.html  
http://localhost:8080/v3/api-docs  

Used switch expression of Java 13 - For returning Signature Algorithm from the Signature fetched from Token Header.

Added to git repo.

Past Changes made :   
========================  
1   REST end points for Todo Application. Entity creation using LOMBOK  
2   Mongo DB integration.  
3   JWT token generation, Validation for all end points except /authenticate.
4   User roles added, Used it for authentication - Read / Write /Admin access added when users getting added.
5   Kafka queue integration, When to do task added , Writing the payload to a topic.
6   Kafka queue listening 


Technology used :
=========================
Spring boot :   2.5.5
Spring Security
Mongodb
Lombok :    1.18.20
OPEN API (Swagger): 1.6.4
Kafka:  2.7.2




