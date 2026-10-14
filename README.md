# HirePath — Online Job Portal
**Tagline:** Find Your Path. Build Your Career.

A Java web application starter for a college project using Java 17, Jakarta Servlets/JSP, JDBC, MySQL, Maven and Tomcat 10.1+.

## Features in this starter
- Job seeker registration and login (passwords hashed with BCrypt)
- Session-based login/logout
- Browse active jobs
- Employers can post jobs after login (starter role model)
- Job seekers can apply to jobs once per job
- View your own applications
- MySQL schema and seed jobs

## Requirements
- JDK 17+
- VS Code with Extension Pack for Java (or another Java IDE)
- Apache Maven
- MySQL Server
- Apache Tomcat 10.1+

## Setup
1. Install the requirements above.
2. Create a MySQL database by running `database/schema.sql` in MySQL Workbench.
3. Set database credentials as environment variables before launching VS Code:
   - Windows PowerShell: `$env:HIREPATH_DB_URL="jdbc:mysql://localhost:3306/hirepath?useSSL=false&serverTimezone=UTC"`
   - `$env:HIREPATH_DB_USER="root"`
   - `$env:HIREPATH_DB_PASSWORD="your-local-mysql-password"`
   Never commit real credentials.
4. Open this folder in VS Code. Run `mvn clean package` in the terminal.
5. Deploy `target/hirepath.war` to Tomcat 10.1+ `webapps` folder and start Tomcat.
6. Visit `http://localhost:8080/hirepath/`.

## Important notes
- This is a learning starter, not production-ready. Add CSRF protection, rate limiting, email verification, stricter validation, audit logging, and security review before any real deployment.
- The seeded jobs are sample data.
- Role-based employer/admin workflows can be expanded for the rubric.
