# User Service

This service is responsible for managing all user-related functionalities for the BookMyShow clone application, including registration, authentication, and profile management.

## Running Locally

To run this service locally, you will need to configure the following environment variables. You can create a `.env` file in the root of this service directory and place the variables there.

### Required Environment Variables

*   `DB_USERNAME`: The username for your MySQL database.
*   `DB_PASSWORD`: The password for your MySQL database.
*   `JWT_SECRET`: A long, random, and secret string used for signing JWT tokens.
*   `MAIL_USERNAME`: The username for your SMTP mail server (e.g., your Gmail address).
*   `MAIL_PASSWORD`: The password for your SMTP mail server (e.g., your Gmail app password).

### Example `.env` file:

```
DB_USERNAME=root
DB_PASSWORD=your_database_password
JWT_SECRET=a-very-long-and-super-secret-key-that-no-one-can-guess
MAIL_USERNAME=youremail@gmail.com
MAIL_PASSWORD=your-gmail-app-password
```

**Note:** For Gmail, you will need to generate an "App Password" from your Google account settings if you have 2-Factor Authentication enabled. Do not use your regular Google account password.