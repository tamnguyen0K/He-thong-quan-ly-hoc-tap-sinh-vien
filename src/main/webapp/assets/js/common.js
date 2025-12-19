/**
 * Common utility functions
 */

function confirmDelete(message) {
    return confirm(message || 'Bạn có chắc muốn xóa?');
}

function formatDate(date) {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('vi-VN');
}

function formatTime(time) {
    if (!time) return '';
    return time.substring(0, 5);
}

// Auto-hide alert messages sau 5 giây
document.addEventListener('DOMContentLoaded', function() {
    const messages = document.querySelectorAll('.message, .success-message, .error-message');
    messages.forEach(function(msg) {
        setTimeout(function() {
            msg.style.transition = 'opacity 0.5s';
            msg.style.opacity = '0';
            setTimeout(function() {
                msg.remove();
            }, 500);
        }, 5000);
    });
});

// Disable button sau khi submit để tránh double submit
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.addEventListener('submit', function() {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Đang xử lý...';
            }
        });
    });
});

