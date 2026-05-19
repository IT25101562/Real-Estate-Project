# Quick Start Guide - Real Estate Property Listings Portal

Get the User Management module up and running in 5 minutes!

## Prerequisites
- Java 11+ installed
- Maven installed
- A modern web browser

## Step 1: Start the Backend Server (3 minutes)

### 1.1 Navigate to Backend Directory
```bash
cd "Real Estate/backend"
```

### 1.2 Build the Project
```bash
mvn clean install
```

### 1.3 Run Spring Boot Application
```bash
mvn spring-boot:run
```

**Expected Output**:
```
Application 'Real Estate Property Listings Portal' started successfully
Tomcat started on port 8080
```

✅ Backend is now running at `http://localhost:8080`

---

## Step 2: Start the Frontend Server (1 minute)

### 2.1 Navigate to Frontend Directory (in a new terminal)
```bash
cd "Real Estate/frontend"
```

### 2.2 Start HTTP Server

**Option A: Using Python 3** (Recommended)
```bash
python3 -m http.server 3000
```

**Option B: Using Node.js**
```bash
npx http-server -p 3000
```

**Expected Output**:
```
Serving HTTP on port 3000...
```

✅ Frontend is now running at `http://localhost:3000`

---

## Step 3: Access the Application (1 minute)

Open your web browser and go to:
```
http://localhost:3000/login.html
```

---

## First Test - Register a New Account

1. Click "Sign up here" link
2. Fill in the form:
   - **First Name**: John
   - **Email**: john@example.com
   - **Password**: Password123
   - **Last Name**: Doe
   - Check "I agree to..." checkbox
3. Click "Create Account"
4. You'll be redirected to your profile page ✅

---

## Second Test - Login

1. On login page, enter:
   - **Email**: john@example.com
   - **Password**: Password123
2. Click "Sign In"
3. You'll be redirected to your profile page ✅

---

## Try These Features

### Update Your Profile
1. Click "✎ Edit Profile" button
2. Update your information (e.g., phone, city)
3. Click "💾 Save Changes"
4. See the updated information displayed ✅

### Change Password
1. Click "🔐 Change Password" button
2. Enter:
   - **Current Password**: Password123
   - **New Password**: NewPassword456
   - **Confirm**: NewPassword456
3. Click "🔄 Update Password" ✅

### Reset Forgotten Password
1. Go back to `http://localhost:3000/login.html`
2. Click "Forgot Password?"
3. Enter your email: john@example.com
4. Click "Send Reset Link"
5. See success message ✅

---

## View H2 Database

If using H2 (default), you can view the database:

1. Open: `http://localhost:8080/h2-console`
2. Connection details:
   - **Driver Class**: `org.h2.Driver`
   - **JDBC URL**: `jdbc:h2:mem:realestatedb`
   - **User Name**: `sa`
   - **Password**: (leave empty)
3. Click "Connect"
4. You'll see the `USERS` table with your registered users ✅

---

## API Testing with cURL

### Test Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123"
  }'
```

### Test Get Profile
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Accept: application/json"
```

---

## Troubleshooting

### Issue: "Address already in use" on port 8080
**Solution**: Change Spring Boot port in `backend/src/main/resources/application.properties`:
```properties
server.port=9090
```

Then update `frontend/assets/js/api.js`:
```javascript
const API_BASE_URL = 'http://localhost:9090/api';
```

---

### Issue: Port 3000 already in use
**Solution**: Use a different port:
```bash
python3 -m http.server 5000
# Then open http://localhost:5000/login.html
```

---

### Issue: "Cannot find database table"
**Solution**: Backend automatically creates tables on startup. Wait 5 seconds and refresh.

---

### Issue: CORS errors in browser console
**Solution**: Make sure both servers are running:
- Backend: `http://localhost:8080` ✅
- Frontend: `http://localhost:3000` ✅

---

### Issue: Form validation not working
**Solution**: Clear browser cache (Ctrl+Shift+Delete or Cmd+Shift+Delete) and refresh

---

## Next Steps

1. 📖 Read [README.md](./README.md) for complete documentation
2. 🔌 Read [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for all API endpoints
3. 🔐 For production: Add password hashing, JWT tokens, and email service
4. 🗄️ Switch to MySQL database for production
5. ✅ Add unit tests and integration tests
6. 🚀 Deploy to cloud (AWS, Azure, Heroku, etc.)

---

## File Structure Reference

```
Real Estate/
├── backend/                    ← Spring Boot application
│   ├── pom.xml               ← Maven configuration
│   └── src/main/java/...     ← Java source code
│
├── frontend/                   ← Web application
│   ├── login.html            ← Login page
│   ├── register.html         ← Registration page
│   ├── forgot-password.html  ← Password reset page
│   ├── profile.html          ← User profile page
│   └── assets/
│       ├── css/styles.css    ← Main stylesheet
│       └── js/               ← JavaScript files
│
├── README.md                 ← Full documentation
├── API_DOCUMENTATION.md      ← API reference
└── QUICK_START.md           ← This file
```

---

## Key Features Demonstrated

✅ User Registration with validation
✅ User Login with session management
✅ User Profile View and Edit
✅ Password Change functionality
✅ Forgot Password (email simulation)
✅ Real-time form validation
✅ Professional olive green theme
✅ Responsive design (mobile-friendly)
✅ RESTful API backend
✅ Spring Boot layered architecture
✅ Error handling and alerts
✅ Local storage for session management

---

## Screenshots & Examples

### Login Page
- Clean card-based design
- Email and password fields
- "Remember me" checkbox
- Links to register and forgot password

### Registration Page
- Multi-step form with validation
- Optional fields for address info
- Terms & conditions checkbox
- Confirmation messages

### Profile Page
- Welcome header with member date
- Quick action buttons
- Inline profile information
- Edit and change password forms

### Styling
- Olive green color scheme (#556B2F)
- Professional typography
- Smooth animations
- Hover effects on buttons
- Alert notifications

---

## Support

For issues or questions:
1. Check the [README.md](./README.md) troubleshooting section
2. Review [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
3. Check browser console for errors (F12)
4. Check backend console for server errors

---

## Performance Tips

- ✅ Backend starts in ~5-10 seconds
- ✅ Frontend loads instantly
- ✅ Page transitions are smooth
- ✅ Form validation is instant
- ✅ API responses are fast (H2 database)

---

**Happy coding! 🎉**

For production deployment, see [README.md](./README.md) Production Deployment Checklist section.

Last Updated: April 26, 2026
