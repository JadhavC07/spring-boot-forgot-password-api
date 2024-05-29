# User Authentication System

This project is a Spring Boot application that handles user authentication with features like user registration, login, password reset using OTP (One-Time Password) sent to the user's email, OTP verification, and password reset functionality.

## Features

- User Registration
- User Login
- Forgot Password
- OTP Verification
- Password Reset

## Technologies Used

- Spring Boot
- Spring Data JPA
- Java Mail Sender
- MySQL Database (can be replaced with other databases like H2, PostgreSQL, etc.)
- Lombok (for reducing boilerplate code)
- Maven

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/user-authentication-system.git
   cd user-authentication-system
2) Configure Database:
The application uses H2 database by default. You can configure another database in application.properties.

3) Update Email Configuration:
Configure your email settings in application.properties:

spring.mail.host=smtp.your-email-provider.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

4) Run the Application:
   mvn spring-boot:run

5) The application will be running at http://localhost:8080.
API Endpoints
Registration
URL: /api/users/register
Method: POST
Request Body:

{
  "username": "user1",
  "password": "password123",
  "email": "user1@example.com"
}

Response: 201 Created

Login
URL: /api/users/login
Method: POST
Request Body:

{
  "username": "user1",
  "password": "password123"
}

Response: 200 OK with user details
Forgot Password

URL: /api/users/forgot-password
Method: POST
Request Body:
{
  "email": "user1@example.com"
}
Response: 200 OK

Verify OTP
URL: /api/users/verify-otp
Method: POST
Request Params:

email: User's email

otp: OTP received in the email

Response: 200 OK

Reset Password
URL: /api/users/reset-password
Method: POST
Request Body:
{
  "email": "user1@example.com",
  "newPassword": "newPassword123"
}
Response: 200 OK

Project Structure
entity: Contains the Users entity class.
repository: Contains the UserRepository interface for database operations.
service: Contains service classes UserService and EmailService.
controller: Contains the UserController class for handling API requests.
payload: Contains DTO classes LoginRequest, ForgotPasswordRequest, and ResetPasswordRequest.

Contribution
Feel free to fork this project and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.
