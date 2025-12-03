# Blog Skeleton (Phase 1) - Minimal Spring Boot Example

This is a deliberately tiny Spring Boot project for learning **session-based** login & role checks
**without** Spring Security.

Tech stack:
- Java 21
- Spring Boot 3.3.4
- Maven
- MySQL 8
- Flyway for DB migration

Main features:
- Login (username/password) and logout.
- Stores the whole `User` object in `HttpSession`.
- Controller methods check `session.getAttribute("user")` and its `role` field to authorize.
- Simple Thymeleaf HTML pages for login and home.

How to run:
1. Create a MySQL database named `blog`.
2. Edit `src/main/resources/application.yml` to set your DB username/password.
3. `mvn spring-boot:run` or build with `mvn package` and run the jar.
4. Flyway will apply `V1__init.sql` to create `blog_user` and insert demo users:
   - admin / 123456 (role ADMIN)
   - guest / 123456 (role GUEST)

Notes:
- Passwords are stored in plain text for simplicity (learning demo). Do NOT use this in production.
- Authorization is a manual session check in controllers (see `AuthController` comments).

