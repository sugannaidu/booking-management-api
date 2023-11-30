# booking-management-api
0. Maven build project:
   ./mvnw clean install   (for linux)

1. You can quickly use the below commands to run a spring boot application from a command line.
1.1. Run the Spring boot application with the java -jar command:
   java -jar booking-api-0.1.0-RELEASE.jar

1.2. Run the Spring boot application using Maven:
   mvn spring-boot:run

2. Access Booking Rest API:  http://localhost:8080/swagger-ui/index.html
2.1. <MUST BE COMPLETED BEFORE #3.1 & #3.2> Initialise data for Booking Rest API:
   POST method http://localhost:8080/api/initialize
2.2. Check room availability:
   GET method http://localhost:8080/api/meeting-room/available
2.3. Make room booking:
   POST method http://localhost:8080/api/meeting-room/booking
