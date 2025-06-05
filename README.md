Multi-Tenant SaaS CRM System Back End

Overview:
-
A secure, multi-tenant Spring Boot API with JWT authentication, role-based database access, automated PDF reporting, and (soon) Kubernetes-ready deployment, designed for scalable SaaS applications with strict data isolation

Features:
-
- Login/Sign up
- Add/update/delete rows from tables
- Add/update/delete constrained by authorities
- User, Manager and Tenant authorities
- User hierarchies
- JJWT authentication
- Spring security technologies for proven security
- Pdf generation based on table relations


(features will be explained more clearly in the future,
but for now this explains it well enough)

Technologies
-
- java 17
- postgres 17
- Docker (optional)
- maven
  - spring boot
  - xhtmlrenderer
  - librepdf
  - io.jsonwebtoken
  - lombok
  - flyway

Setup
-
1.  ```./mvn clean package``` to create jar (on project root directory)
2. It is recommended to make a docker image for portability
3. Host on a server that accepts requests from client server that will utilize the application


See working version at **https://luckylondonwebstudio.shop/**
Front end code for this server app at **https://github.com/S1coding/Multi-Tenant-SaaS-CRM-System-front-end**
