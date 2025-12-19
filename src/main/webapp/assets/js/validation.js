/**
 * Validation functions cho các form
 */

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validatePassword(password) {
    return password.length >= 6;
}

function validateRequired(fields) {
    for (let field of fields) {
        if (!field.value || field.value.trim() === '') {
            showError(field, 'Trường này là bắt buộc');
            return false;
        }
    }
    return true;
}

function showError(field, message) {
    clearError(field);
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.color = '#c33';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;
    field.parentNode.appendChild(errorDiv);
    field.style.borderColor = '#c33';
}

function clearError(field) {
    const errorDiv = field.parentNode.querySelector('.error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
    field.style.borderColor = '#ddd';
}

// Validate form đăng ký
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.querySelector('form[action*="/register"]');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            const email = registerForm.querySelector('input[name="email"]');
            const password = registerForm.querySelector('input[name="password"]');
            const confirmPassword = registerForm.querySelector('input[name="confirmPassword"]');
            
            let isValid = true;
            
            if (!validateEmail(email.value)) {
                showError(email, 'Email không hợp lệ');
                isValid = false;
            }
            
            if (!validatePassword(password.value)) {
                showError(password, 'Mật khẩu phải có ít nhất 6 ký tự');
                isValid = false;
            }
            
            if (password.value !== confirmPassword.value) {
                showError(confirmPassword, 'Mật khẩu không khớp');
                isValid = false;
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });
    }
});

