document.addEventListener('DOMContentLoaded', () => {
    // Interactivity for filter buttons
    const reviewsContainer = document.getElementById('reviews-container');
    
    // Fetch and render reviews
    fetch('/api/reviews/approved')
        .then(res => res.json())
        .then(data => {
            renderReviews(data);
            attachFilterLogic();
        })
        .catch(err => console.error("Error fetching approved reviews:", err));

    function renderReviews(reviews) {
        if (!reviewsContainer) return;
        reviewsContainer.innerHTML = '';
        
        if (reviews.length === 0) {
            reviewsContainer.innerHTML = '<p>No reviews yet.</p>';
            return;
        }

        reviews.forEach(review => {
            const article = document.createElement('article');
            article.className = 'review-card';
            
            let starsHtml = '';
            for(let i=0; i<5; i++) {
                if (i < review.rating) {
                    starsHtml += '<i class="fa-solid fa-star"></i>';
                } else {
                    starsHtml += '<i class="fa-regular fa-star"></i>';
                }
            }

            const initial = review.name ? review.name.charAt(0).toUpperCase() : 'G';
            
            article.innerHTML = `
                <div class="review-card-header">
                    <div class="reviewer-profile">
                        <div class="avatar" style="background-color: #3b82f6;">${initial}</div>
                        <div class="reviewer-info">
                            <h4>${review.name || 'Guest User'}</h4>
                            <span class="reviewer-type verified"><i class="fa-solid fa-circle-check"></i> Verified Review</span>
                        </div>
                    </div>
                    <div class="review-date">${review.date || 'Recent'}</div>
                </div>
                
                <div class="review-stars">
                    ${starsHtml}
                </div>
                
                <h3 class="review-title">${review.title || 'Review'}</h3>
                <p class="review-comment">${review.comment}</p>
            `;
            reviewsContainer.appendChild(article);
        });
    }

    function attachFilterLogic() {
        const filterBtns = document.querySelectorAll('.filter-btn');
        const cards = document.querySelectorAll('.review-card');

        filterBtns.forEach(btn => {
            btn.addEventListener('click', (e) => {
                // Manage active states
                filterBtns.forEach(b => b.classList.remove('active'));
                e.target.classList.add('active');

                const filterType = e.target.textContent;

                // Simple filtering logic with animations
                cards.forEach(card => {
                    const isVerified = card.querySelector('.reviewer-type').classList.contains('verified');
                    
                    // Animate out
                    card.style.transition = 'all 0.2s ease-out';
                    card.style.opacity = '0';
                    card.style.transform = 'scale(0.97) translateY(10px)';
                    
                    setTimeout(() => {
                        if (filterType === 'Verified Only' && !isVerified) {
                            card.style.display = 'none';
                        } else {
                            card.style.display = 'block';
                            
                            // Force reflow
                            card.offsetHeight;
                            
                            // Animate in
                            card.style.transition = 'all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1)';
                            card.style.opacity = '1';
                            card.style.transform = 'scale(1) translateY(0)';
                        }
                    }, 200);
                });
            });
        });
    }
});
