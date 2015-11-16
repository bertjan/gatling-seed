gatling-seed
==========
This project is a skeleton for performance tests with Gatling and Maven. You can use it to quickly bootstrap your Gatling test setup and development environment.


Getting started
---
Install the following dependencies:  
- Java 8 (Java 7 will probably work, not tested though)
- Maven 3


Recording a test
---
To record a test, perform the following steps:  
- Click through the test path in your browser
- Export the request flow as HAR file: standard available in Chrome, available as plugin in Firefox through a FireBug extension: http://www.softwareishard.com/blog/netexport/
- Add details of the HAR filename and simulation name in ```src/main/resources/config/gatling-recorder.json```
- Generate the Gatling script: ```mvn clean scala:run```
- The Gatling simulation will be generated in ```src/test/scala/simulations```. Accompanying request bodies (in case of POST/PUT requests) will be in ```src/test/resources/request-bodies``` 

Executing a test
---
To execute a test, perform:   
```mvn clean gatling:execute```  
  

Or, when multiple simulations present:  
```mvn gatling:execute -Dgatling.simulationClass=simulations.<simulationClassName>```
