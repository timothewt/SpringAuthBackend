# Java Spring Backend with Authentication System
This repository provides a foundational implementation of a backend using Java Spring (v3.3.4), featuring a fully functional authentication system. It supports user sign-up, sign-in, and sign-out, along with persistent authentication using tokens. The setup includes a basic API structure that can be easily extended for more complex applications.

## Features
- User Authentication: Secure login, registration, and logout flows using JWT.
- Persistent Authentication: Sessions remain active even after refreshing or closing the application using Refresh Tokens.
- API Structure: Organized to facilitate the integration of additional features like user roles and profile management.

## Technologies Used
- Java Spring: v3.3.4 – A powerful framework for building robust backend applications.
- Spring Security: For secure authentication and authorization.

## Getting Started
To run this project locally:
1. Clone the repository:
```bash
git clone https://github.com/timothewt/springauthbackend.git
```
2. Build the project:
```bash
./mvnw clean install
```
3. Run the application:
```bash
./mvnw spring-boot:run
```
The API will be accessible at `http://localhost:8080`.

## Reference
[Spring boot 3.0 - Secure your API with JWT Token - Ali Bouali](https://www.youtube.com/watch?v=BVdQ3iuovg0)
