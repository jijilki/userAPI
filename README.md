Work Log
=============================
16-Feb-2022
Dropped plan of Spring security config with Authentication Builder manager, Instead used existing mechanism with slight improvement with GA roles etc. 

22-Jan-2022
Proper registration and login process implementation to be done.
As part of it doing some code clean up : 
    1. Separate mode , entity , config , controller classes.

20-Jan-2022 - 22-Jan
Kafka issue : With default java missing in Ubuntu .Fixed
ERROR org.apache.kafka.common.errors.InvalidReplicationFactorException: Replication factor: 1 larger than available brokers: 0.
Fixing the Kafka issue pending.

16-Jan-2022  
Password encoding using Bcrypt. On Registration and Authentication  

Temporarily , just bcrypt with encoding , and while authenticating - password.encoder("rawpassword,encryptedPassword) -> returns boolean to verify the password. Better implementation is required.
https://github.com/Baeldung/spring-security-registration  


15-Jan-2022  
Added OPEN API Swagger dependencies & Configurations added in the security configurations for URLs.  
http://localhost:8080/swagger-ui/index.html  
http://localhost:8080/v3/api-docs  

Used switch expression of Java 13 - For returning Signature Algorithm from the Signature fetched from Token Header.
Switch for patterns. Java 17

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
Java 17 - Open JDK  
Spring boot :   2.5.5  
Spring Security  
Mongodb  
Lombok :    1.18.20  
OPEN API (Swagger): 1.6.4  
Kafka:  2.7.2  




Todo :
Learn Runners and Post Pre scripting in POSTMAN.