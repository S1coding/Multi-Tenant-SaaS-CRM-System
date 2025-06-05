Multi-Tenant SaaS CRM System Back End

Features:
-
- Add/Update contacts to database
- Add/Update tenants to database
- Add/Update deals to database
- Add/Update tasks to database
- Generate report pdf from database tables

Technologies
-
- java 17
- spring boot
- xhtmlrenderer
- librepdf
- io.jsonwebtoken
- lombok
- flyway
- postgres 17
- maven

Setup
-
1.  ```./mvn clean package``` to create jar
2. It is recommended to make a docker image for portability
3. Host on a server that accepts requests from client server that will utilize the application.

    
See working version at **https://luckylondonwebstudio.shop/**
