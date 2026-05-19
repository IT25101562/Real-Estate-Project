document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('review-form');
    const submitBtn = document.getElementById('submit-btn');
    const successMessage = document.getElementById('success-message');
    const resetBtn = document.getElementById('reset-btn');
    const stars = document.querySelectorAll('input[name="rating"]');
    const ratingError = document.getElementById('rating-error');
    
    // Add micro-interactions for stars
    stars.forEach(star => {
        star.addEventListener('change', (e) => {
            // Hide error if shown
            ratingError.classList.remove('show');
            
            // Pop effect
            const starLabel = document.querySelector(`label[for="${e.target.id}"]`);
            starLabel.style.transform = 'scale(1.3)';
            setTimeout(() => {
                if(e.target.checked) starLabel.style.transform = 'scale(1.15)';
            }, 150);
        });
    });

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        
        // Validate rating
        const rating = document.querySelector('input[name="rating"]:checked');
        if (!rating) {
            ratingError.classList.add('show');
            const starContainer = document.getElementById('star-rating');
            starContainer.style.animation = 'shake 0.5s cubic-bezier(0.36, 0.07, 0.19, 0.97) both';
            setTimeout(() => {
                starContainer.style.animation = '';
            }, 500);
            return;
        }
        const title = document.getElementById('review-title').value;
        const comment = document.getElementById('review-content').value;
        
        const reviewData = {
            rating: parseInt(rating.value),
            title: title,
            comment: comment
        };

        // Real API call
        submitBtn.classList.add('loading');
        submitBtn.querySelector('i').className = 'fa-solid fa-spinner';
        submitBtn.querySelector('span').textContent = 'Submitting...';

        fetch('/api/reviews', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewData)
        })
        .then(response => {
            if (response.ok) {
                // Success state
                submitBtn.classList.remove('loading');
                submitBtn.querySelector('i').className = 'fa-solid fa-arrow-right';
                submitBtn.querySelector('span').textContent = 'Submit Review';
                
                successMessage.classList.add('active');
            } else {
                throw new Error("Failed to submit review");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            submitBtn.classList.remove('loading');
            submitBtn.querySelector('i').className = 'fa-solid fa-arrow-right';
            submitBtn.querySelector('span').textContent = 'Try Again';
            alert('Failed to submit review. Please try again.');
        });
    });

    resetBtn.addEventListener('click', () => {
        successMessage.classList.remove('active');
        form.reset();
        // Uncheck stars manually since we might have custom styles based on checked state
        stars.forEach(s => s.checked = false);
        
        // Remove focus from fields
        document.querySelectorAll('input, textarea').forEach(el => el.blur());
    });
});

// Add shake animation explicitly
const style = document.createElement('style');
style.innerHTML = `
    @keyframes shake {
        10%, 90% { transform: translate3d(-1px, 0, 0); }
        20%, 80% { transform: translate3d(2px, 0, 0); }
        30%, 50%, 70% { transform: translate3d(-4px, 0, 0); }
        40%, 60% { transform: translate3d(4px, 0, 0); }
    }
`;
document.head.appendChild(style);
