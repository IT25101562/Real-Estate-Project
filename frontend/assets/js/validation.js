/**
 * Form Validation Functions
 * Validates form inputs and displays error messages
 */

/**
 * Email validation regex pattern
 */
const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

/**
 * Password validation regex pattern (minimum 6 characters)
 */
const PASSWORD_PATTERN = /.{6,}/;

/**
 * Phone number validation regex pattern (US format)
 */
const PHONE_PATTERN = /^(\+1)?[-.\s]?\(?[0-9]{3}\)?[-.\s]?[0-9]{3}[-.\s]?[0-9]{4}$/;

/**
 * ZIP code validation regex pattern (US format)
 */
const ZIP_PATTERN = /^\d{5}(-\d{4})?$/;

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} - True if email is valid
 */
function validateEmail(email) {
    return EMAIL_PATTERN.test(email);
}

/**
 * Validate password strength
 * @param {string} password - Password to validate
 * @returns {boolean} - True if password meets requirements
 */
function validatePassword(password) {
    return PASSWORD_PATTERN.test(password);
}

/**
 * Validate required field is not empty
 * @param {string} value - Value to check
 * @returns {boolean} - True if value is not empty
 */
function validateRequired(value) {
    return value && value.trim().length > 0;
}

/**
 * Validate field matches another field (e.g., password confirmation)
 * @param {string} value1 - First value
 * @param {string} value2 - Second value
 * @returns {boolean} - True if values match
 */
function validateMatch(value1, value2) {
    return value1 === value2;
}

/**
 * Validate field length is within range
 * @param {string} value - Value to check
 * @param {number} min - Minimum length
 * @param {number} max - Maximum length
 * @returns {boolean} - True if length is within range
 */
function validateLength(value, min, max) {
    const length = value ? value.trim().length : 0;
    return length >= min && length <= max;
}

/**
 * Validate phone number format
 * @param {string} phone - Phone number to validate
 * @returns {boolean} - True if phone is valid
 */
function validatePhone(phone) {
    if (!phone) return true; // Optional field
    return PHONE_PATTERN.test(phone);
}

/**
 * Validate ZIP code format
 * @param {string} zip - ZIP code to validate
 * @returns {boolean} - True if ZIP is valid
 */
function validateZip(zip) {
    if (!zip) return true; // Optional field
    return ZIP_PATTERN.test(zip);
}

/**
 * Add error state to form field
 * @param {HTMLElement} element - Form element
 * @param {string} message - Error message to display
 */
function setFieldError(element, message) {
    const formGroup = element.closest('.form-group');
    if (!formGroup) return;

    formGroup.classList.remove('success');
    formGroup.classList.add('error');

    const errorMessage = formGroup.querySelector('.error-message');
    if (errorMessage) {
        errorMessage.textContent = message;
    }
}

/**
 * Add success state to form field
 * @param {HTMLElement} element - Form element
 * @param {string} message - Success message to display (optional)
 */
function setFieldSuccess(element, message = '') {
    const formGroup = element.closest('.form-group');
    if (!formGroup) return;

    formGroup.classList.remove('error');
    formGroup.classList.add('success');

    if (message) {
        const successMessage = formGroup.querySelector('.success-message');
        if (successMessage) {
            successMessage.textContent = message;
        }
    }
}

/**
 * Clear field validation state
 * @param {HTMLElement} element - Form element
 */
function clearFieldError(element) {
    const formGroup = element.closest('.form-group');
    if (!formGroup) return;

    formGroup.classList.remove('error', 'success');

    const errorMessage = formGroup.querySelector('.error-message');
    if (errorMessage) {
        errorMessage.textContent = '';
    }
}

/**
 * Validate a single form field
 * @param {HTMLElement} field - Form field to validate
 * @param {object} rules - Validation rules for this field
 * @returns {boolean} - True if field is valid
 */
function validateField(field, rules) {
    const value = field.value.trim();
    const fieldName = rules.name || field.name || field.id;

    // Check required rule
    if (rules.required && !validateRequired(value)) {
        setFieldError(field, `${fieldName} is required`);
        return false;
    }

    // If field is empty and not required, it's valid
    if (!value && !rules.required) {
        clearFieldError(field);
        return true;
    }

    // Check email rule
    if (rules.email && !validateEmail(value)) {
        setFieldError(field, `${fieldName} must be a valid email address`);
        return false;
    }

    // Check password rule
    if (rules.password && !validatePassword(value)) {
        setFieldError(field, `${fieldName} must be at least 6 characters long`);
        return false;
    }

    // Check minLength rule
    if (rules.minLength && !validateLength(value, rules.minLength, Infinity)) {
        setFieldError(field, `${fieldName} must be at least ${rules.minLength} characters`);
        return false;
    }

    // Check maxLength rule
    if (rules.maxLength && !validateLength(value, 0, rules.maxLength)) {
        setFieldError(field, `${fieldName} must not exceed ${rules.maxLength} characters`);
        return false;
    }

    // Check match rule
    if (rules.match) {
        const matchField = document.querySelector(rules.match);
        if (matchField && !validateMatch(value, matchField.value)) {
            setFieldError(field, `${fieldName} does not match`);
            return false;
        }
    }

    // Check phone rule
    if (rules.phone && !validatePhone(value)) {
        setFieldError(field, `${fieldName} must be a valid phone number`);
        return false;
    }

    // Check zip rule
    if (rules.zip && !validateZip(value)) {
        setFieldError(field, `${fieldName} must be a valid ZIP code`);
        return false;
    }

    setFieldSuccess(field);
    return true;
}

/**
 * Validate entire form
 * @param {HTMLElement} form - Form element to validate
 * @param {object} rules - Validation rules for form fields
 * @returns {boolean} - True if all fields are valid
 */
function validateForm(form, rules) {
    let isValid = true;

    Object.keys(rules).forEach(fieldName => {
        const field = form.querySelector(`[name="${fieldName}"]`);
        if (field) {
            if (!validateField(field, rules[fieldName])) {
                isValid = false;
            }
        }
    });

    return isValid;
}

/**
 * Setup real-time field validation
 * @param {HTMLElement} field - Form field to setup
 * @param {object} rules - Validation rules for this field
 */
function setupFieldValidation(field, rules) {
    // Validate on blur
    field.addEventListener('blur', function() {
        validateField(this, rules);
    });

    // Clear error on input
    field.addEventListener('input', function() {
        const formGroup = this.closest('.form-group');
        if (formGroup && formGroup.classList.contains('error')) {
            clearFieldError(this);
        }
    });
}

/**
 * Setup real-time validation for all fields in a form
 * @param {HTMLElement} form - Form element
 * @param {object} rules - Validation rules for form fields
 */
function setupFormValidation(form, rules) {
    Object.keys(rules).forEach(fieldName => {
        const field = form.querySelector(`[name="${fieldName}"]`);
        if (field) {
            setupFieldValidation(field, rules[fieldName]);
        }
    });
}
