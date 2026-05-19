/**
 * Common Utility Functions
 * Shared utilities for all pages
 */

/**
 * Show alert/message to user
 * @param {string} message - Message to display
 * @param {string} type - Alert type: 'success', 'error', 'warning', 'info'
 * @param {number} duration - Duration to show alert in milliseconds (0 = permanent)
 */
function showAlert(message, type = 'info', duration = 5000) {
    // Remove existing alerts
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());

    // Create alert element
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} fade-in`;

    // Icon mapping
    const iconMap = {
        success: '✓',
        error: '✕',
        warning: '⚠',
        info: 'ℹ'
    };

    alertDiv.innerHTML = `
        <span class="alert-icon">${iconMap[type]}</span>
        <span>${message}</span>
        <button type="button" class="alert-close">&times;</button>
    `;

    // Find or create alert container
    let alertContainer = document.querySelector('.alert-container');
    if (!alertContainer) {
        alertContainer = document.createElement('div');
        alertContainer.className = 'alert-container';
        alertContainer.style.position = 'fixed';
        alertContainer.style.top = '80px';
        alertContainer.style.left = '50%';
        alertContainer.style.transform = 'translateX(-50%)';
        alertContainer.style.zIndex = '1000';
        alertContainer.style.maxWidth = '500px';
        alertContainer.style.width = '90%';
        document.body.appendChild(alertContainer);
    }

    alertContainer.appendChild(alertDiv);

    // Close button handler
    const closeBtn = alertDiv.querySelector('.alert-close');
    closeBtn.addEventListener('click', () => {
        alertDiv.remove();
    });

    // Auto-remove after duration
    if (duration > 0) {
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.remove();
            }
        }, duration);
    }
}

/**
 * Show loading spinner
 * @param {string} containerId - ID of container where spinner will be shown
 */
function showLoadingSpinner(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = '<div class="spinner"></div>';
}

/**
 * Hide loading spinner
 * @param {string} containerId - ID of container where spinner is shown
 */
function hideLoadingSpinner(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    const spinner = container.querySelector('.spinner');
    if (spinner) {
        spinner.remove();
    }
}

/**
 * Format date to readable string
 * @param {string|Date} date - Date to format
 * @returns {string} - Formatted date string
 */
function formatDate(date) {
    if (!date) return '';

    const dateObj = typeof date === 'string' ? new Date(date) : date;
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    };

    return dateObj.toLocaleDateString('en-US', options);
}

/**
 * Format date to short format (MM/DD/YYYY)
 * @param {string|Date} date - Date to format
 * @returns {string} - Formatted date string
 */
function formatDateShort(date) {
    if (!date) return '';

    const dateObj = typeof date === 'string' ? new Date(date) : date;
    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
    const day = String(dateObj.getDate()).padStart(2, '0');
    const year = dateObj.getFullYear();

    return `${month}/${day}/${year}`;
}

/**
 * Debounce function - delay function execution
 * @param {Function} func - Function to debounce
 * @param {number} delay - Delay in milliseconds
 * @returns {Function} - Debounced function
 */
function debounce(func, delay = 300) {
    let timeoutId;
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
}

/**
 * Throttle function - limit function execution
 * @param {Function} func - Function to throttle
 * @param {number} limit - Time limit in milliseconds
 * @returns {Function} - Throttled function
 */
function throttle(func, limit = 300) {
    let inThrottle;
    return function(...args) {
        if (!inThrottle) {
            func.apply(this, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

/**
 * Get URL query parameters
 * @returns {object} - Object containing query parameters
 */
function getQueryParams() {
    const params = {};
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.forEach((value, key) => {
        params[key] = value;
    });
    return params;
}

/**
 * Get single query parameter
 * @param {string} paramName - Name of parameter to get
 * @returns {string|null} - Parameter value or null
 */
function getQueryParam(paramName) {
    const params = getQueryParams();
    return params[paramName] || null;
}

/**
 * Copy text to clipboard
 * @param {string} text - Text to copy
 * @returns {Promise} - Promise that resolves when copy is complete
 */
function copyToClipboard(text) {
    return navigator.clipboard.writeText(text).then(() => {
        showAlert('Copied to clipboard', 'success', 2000);
    }).catch(() => {
        showAlert('Failed to copy to clipboard', 'error');
    });
}

/**
 * Clear form fields
 * @param {HTMLElement} form - Form element to clear
 */
function clearForm(form) {
    form.reset();
    form.querySelectorAll('.form-group').forEach(group => {
        group.classList.remove('error', 'success');
        const errorMsg = group.querySelector('.error-message');
        if (errorMsg) errorMsg.textContent = '';
        const successMsg = group.querySelector('.success-message');
        if (successMsg) successMsg.textContent = '';
    });
}

/**
 * Disable form inputs
 * @param {HTMLElement} form - Form element
 */
function disableForm(form) {
    const inputs = form.querySelectorAll('input, textarea, select, button');
    inputs.forEach(input => input.disabled = true);
}

/**
 * Enable form inputs
 * @param {HTMLElement} form - Form element
 */
function enableForm(form) {
    const inputs = form.querySelectorAll('input, textarea, select, button');
    inputs.forEach(input => input.disabled = false);
}

/**
 * Check if string is empty or only whitespace
 * @param {string} str - String to check
 * @returns {boolean} - True if string is empty
 */
function isEmpty(str) {
    return !str || str.trim().length === 0;
}

/**
 * Truncate string to specified length
 * @param {string} str - String to truncate
 * @param {number} length - Max length
 * @param {string} suffix - Suffix to add (default: '...')
 * @returns {string} - Truncated string
 */
function truncateString(str, length, suffix = '...') {
    if (!str) return '';
    if (str.length <= length) return str;
    return str.substring(0, length - suffix.length) + suffix;
}

/**
 * Initialize page - setup common event listeners
 */
function initPage() {
    // Setup logout button if it exists
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logoutUser();
            window.location.href = 'login.html';
        });
    }

    // Setup dark mode toggle if it exists
    const darkModeToggle = document.getElementById('dark-mode-toggle');
    if (darkModeToggle) {
        darkModeToggle.addEventListener('click', toggleDarkMode);
    }
}

/**
 * Toggle dark mode
 */
function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    const isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDarkMode);
}

/**
 * Initialize dark mode from localStorage
 */
function initDarkMode() {
    const isDarkMode = localStorage.getItem('darkMode') === 'true';
    if (isDarkMode) {
        document.body.classList.add('dark-mode');
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    initPage();
    initDarkMode();
});
