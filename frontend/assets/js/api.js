/**
 * API Configuration and Helper Functions
 * Centralized API communication with backend
 */

// API Base URL - Change this if backend runs on different port/host
const API_BASE_URL = 'http://localhost:8080/api';
const AUTH_ENDPOINTS = {
    register: '/auth/register',
    login: '/auth/login',
    forgotPassword: '/auth/forgot-password',
    health: '/auth/health'
};

const USER_ENDPOINTS = {
    getProfile: (userId) => `/users/${userId}`,
    updateProfile: (userId) => `/users/${userId}`,
    resetPassword: (userId) => `/users/${userId}/reset-password`,
    getAllUsers: '/users',
    deleteUser: (userId) => `/users/${userId}`
};

/**
 * Generic API request function
 * @param {string} endpoint - API endpoint path
 * @param {string} method - HTTP method (GET, POST, PUT, DELETE)
 * @param {object} data - Request body data (optional)
 * @returns {Promise} - Promise resolving to response data
 */
async function apiRequest(endpoint, method = 'GET', data = null) {
    try {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        };

        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

        // Parse response
        const responseData = await response.json();

        if (!response.ok) {
            throw {
                status: response.status,
                message: responseData.message || 'An error occurred',
                errors: responseData.errors || null
            };
        }

        return responseData;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

/**
 * User Registration API Call
 * @param {object} registrationData - Registration form data
 * @returns {Promise} - Promise resolving to user response data
 */
async function registerUser(registrationData) {
    return apiRequest(AUTH_ENDPOINTS.register, 'POST', registrationData);
}

/**
 * User Login API Call
 * @param {string} email - User email
 * @param {string} password - User password
 * @returns {Promise} - Promise resolving to user response data
 */
async function loginUser(email, password) {
    const loginData = { email, password };
    return apiRequest(AUTH_ENDPOINTS.login, 'POST', loginData);
}

/**
 * Forgot Password API Call
 * @param {string} email - User email
 * @returns {Promise} - Promise resolving to success message
 */
async function forgotPassword(email) {
    const data = { email };
    return apiRequest(AUTH_ENDPOINTS.forgotPassword, 'POST', data);
}

/**
 * Get User Profile API Call
 * @param {number} userId - User ID
 * @returns {Promise} - Promise resolving to user profile data
 */
async function getUserProfile(userId) {
    return apiRequest(USER_ENDPOINTS.getProfile(userId), 'GET');
}

/**
 * Update User Profile API Call
 * @param {number} userId - User ID
 * @param {object} profileData - Updated profile data
 * @returns {Promise} - Promise resolving to updated user data
 */
async function updateUserProfile(userId, profileData) {
    return apiRequest(USER_ENDPOINTS.updateProfile(userId), 'PUT', profileData);
}

/**
 * Reset Password API Call
 * @param {number} userId - User ID
 * @param {string} newPassword - New password
 * @returns {Promise} - Promise resolving to user response data
 */
async function resetPassword(userId, newPassword) {
    const data = { newPassword };
    return apiRequest(USER_ENDPOINTS.resetPassword(userId), 'POST', data);
}

/**
 * Get all users API Call
 * @returns {Promise} - Promise resolving to list of users
 */
async function getAllUsers() {
    return apiRequest(USER_ENDPOINTS.getAllUsers, 'GET');
}

/**
 * Delete User API Call
 * @param {number} userId - User ID
 * @returns {Promise} - Promise resolving to success message
 */
async function deleteUser(userId) {
    return apiRequest(USER_ENDPOINTS.deleteUser(userId), 'DELETE');
}

/**
 * Check API Health
 * @returns {Promise} - Promise resolving to health status
 */
async function checkApiHealth() {
    return apiRequest(AUTH_ENDPOINTS.health, 'GET');
}

/**
 * Store user session data in localStorage
 * @param {object} userData - User data to store
 */
function storeUserSession(userData) {
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('userId', userData.id);
    localStorage.setItem('userEmail', userData.email);
}

/**
 * Retrieve user session data from localStorage
 * @returns {object|null} - Stored user data or null
 */
function getUserSession() {
    const userData = localStorage.getItem('user');
    return userData ? JSON.parse(userData) : null;
}

/**
 * Check if user is logged in
 * @returns {boolean} - True if user session exists
 */
function isUserLoggedIn() {
    return localStorage.getItem('user') !== null;
}

/**
 * Logout user by clearing session data
 */
function logoutUser() {
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
    localStorage.removeItem('userEmail');
}

/**
 * Redirect to login if user is not logged in
 */
function requireLogin() {
    if (!isUserLoggedIn()) {
        window.location.href = 'login.html';
    }
}

/**
 * Redirect to profile if user is logged in
 */
function redirectIfLoggedIn() {
    if (isUserLoggedIn()) {
        const user = getUserSession();
        window.location.href = `profile.html?userId=${user.id}`;
    }
}
