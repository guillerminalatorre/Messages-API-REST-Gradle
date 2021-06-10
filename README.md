# Messages-API-REST-Gradle

## API Features

### Message
- Delete action by User, not by Message.
- BCC (Blind Copy Carbon) not shows on Inbox messages. Otherwise it shows in Sent messages.
- Attachments are links.

### Profile: Client
- It's created from an existient Employee.
- SignIn.
- Login.
- Logout.
- Send and recieve messages with another users. 
- Create Labels or use defaults.
- Assign Labels to Message.
- Filter "Sent" and "Inbox" by labels. 
- Search other users by aproximation. 
- Update password or Username.


### Profile: Admin
- Search "Sent" and "Inbox" by user.
- Make an User an Admin.
- Delete Users.

## Features
- API Rest.
- Data persistence in local PostgreSQL database.
- Data access by Spring Boot JPA.
- Hibernate.
- Postman for endpoints view: https://www.getpostman.com/collections/03cc0b1fe42d5ef9e4b6

### Technical Features
- Spring Boot 2.4.5
- Java 11.0.11
- Gradle 7.0.2

## Installation
Create local PostgreSQL database and configure src/main/resources/application.properties or use my config. Run to create tables.

To install dependencies:
```sh
gradlew build
```

### To run:
```sh
gradlew bootRun
```
## Docker

```sh
dockek compose up
```

## Testing
```sh
cd (repository_location)
gradlew test
gradlew jacocoTestCoverageVerification
```
Check build/reports/jacoco/test/html/index.html to see Jacoco's report.

