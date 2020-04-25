# springboot-json-comparator
Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements
For building and running the application you need:
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

# Steps to run

1. run the spring boot app

2. open h2-console at :- http://localhost:8080/h2-console/

-  add following query in h2 -console

-  drop table json_db;
  CREATE TABLE JSON_DB(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  json_data VARCHAR2 (20000 byte) NOT NULL
);


3.  call the   "post" service for saving of data:-   http://localhost:8080/api/v1/json  
	with body as given in model package

4. 	call the   "post" service for comparing :-   http://localhost:8080/api/v1/compare-json  
	with body as given in model package
 
 

   