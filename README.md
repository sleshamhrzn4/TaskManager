# Task Management App

Task Management App is a web application that lets users register, log in, and manage their tasks. Each task can have a description, priority, and deadline, and once a task is created, users are restricted from closing it until that deadline is reached, encouraging accountability and preventing tasks from being marked done prematurely.

## Tech Stack
Java · Servlets/JSP/JSTL · MySQL · JDBC · HTML/CSS · Tomcat · Maven

## Setup
1. Clone the repo and open it in IntelliJ (auto-imports via `pom.xml`).
2. Create the DB: `CREATE DATABASE task_management_db;` and import the schema from `/db` or `/sql`.
3. Set your DB credentials in `db.properties` / `DBConnection.java`.
4. Add a Tomcat run config (Run > Edit Configurations > Tomcat Server), deploy the artifact.
5. Run, then visit: `http://localhost:8080/task-management-app/login`

## Folder Structure
```
src/main/
├── java/
│   ├── controller/   # Servlets
│   ├── dao/          # DB access
│   ├── model/         # User, Task
│   └── util/          # DB connection, helpers
└── webapp/
    ├── css/
    └── WEB-INF/views/  # JSPs
```

## API Endpoints
Method     Endpoint             Description
GET/POST  /register     – Show registration page / create user
GET/POST  /login        – Show login page / authenticate
GET       /dashboard    – Show dashboard
GET/POST  /task         – List / create / update / close / delete tasks
GET       /logout       – End session

## Known Limitations
- Tasks can only be closed after their deadline passes.

## Future Improvements
- Admin-approved early closure
- Email reminders
- Password reset
- Role-based access
- Categories/priorities/filtering
- REST + JSON API
- Tests

#Screenshots 

<img width="1911" height="960" alt="image" src="https://github.com/user-attachments/assets/a5e02228-740e-4b47-b0ca-292151f4d56f" />
<img width="1913" height="968" alt="image" src="https://github.com/user-attachments/assets/8f551c34-00d9-4dce-8cd8-4cd56bcda175" />
<img width="1917" height="962" alt="image" src="https://github.com/user-attachments/assets/a9f2ac24-7087-48f9-bc1e-d013804a2c4d" />
<img width="1907" height="866" alt="image" src="https://github.com/user-attachments/assets/312e48a3-1f9a-455d-a860-3544482a6694" />
<img width="1916" height="858" alt="image" src="https://github.com/user-attachments/assets/6895365f-b992-414a-95b0-9e4d9e637561" />
<img width="1897" height="870" alt="image" src="https://github.com/user-attachments/assets/1b9b2114-af16-414c-b0bb-a79b29d0607f" />
<img width="1915" height="862" alt="image" src="https://github.com/user-attachments/assets/2dd6ee56-6a3e-4316-b396-5e074285fbc9" />
<img width="1915" height="865" alt="image" src="https://github.com/user-attachments/assets/6f5379c8-3a8c-44a1-be78-a66ae3d23146" />
<img width="1911" height="858" alt="image" src="https://github.com/user-attachments/assets/90ec395f-bec6-4404-9e42-eae45463b890" />
<img width="1911" height="862" alt="image" src="https://github.com/user-attachments/assets/c9439551-1311-4259-be79-18b004cb436c" />
<img width="1913" height="861" alt="image" src="https://github.com/user-attachments/assets/a07b34b6-443f-4024-ae8c-69aca12fa64e" />
<img width="1915" height="868" alt="image" src="https://github.com/user-attachments/assets/065eab96-e597-4cdc-8e80-dd0cb1dac928" />
<img width="1907" height="861" alt="image" src="https://github.com/user-attachments/assets/d35acda5-dde2-470f-8d6f-2140cce0d7f3" />

