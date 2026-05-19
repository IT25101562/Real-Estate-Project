document.addEventListener('DOMContentLoaded', () => {
    // Data array
    let reviewsData = [];

    const tableBody = document.getElementById('reviews-table-body');
    const searchInput = document.getElementById('search-input');
    const ratingFilter = document.getElementById('rating-filter');
    const emptyState = document.getElementById('empty-state');
    const tableContainer = document.querySelector('.data-table');
    
    // Modal Elements
    const editModal = document.getElementById('edit-modal');
    const closeModalBtns = document.querySelectorAll('.close-modal, #cancel-edit');
    const editForm = document.getElementById('edit-form');
    const editText = document.getElementById('edit-text');
    const editStatus = document.getElementById('edit-status');
    let currentEditingId = null;

    // Render table
    function renderTable(data) {
        tableBody.innerHTML = '';
        
        if (data.length === 0) {
            tableContainer.style.display = 'none';
            emptyState.style.display = 'block';
            return;
        }
        
        tableContainer.style.display = 'table';
        emptyState.style.display = 'none';

        data.forEach(review => {
            const tr = document.createElement('tr');
            tr.dataset.id = review.id;
            
            // Stars HTML
            let starsHtml = '';
            for(let i = 0; i < 5; i++) {
                if(i < review.rating) {
                    starsHtml += '<i class="fa-solid fa-star"></i>';
                } else {
                    starsHtml += '<i class="fa-regular fa-star" style="color:var(--star-inactive)"></i>';
                }
            }

            // Determine status class securely
            const s = review.status.toLowerCase();
            const statusClass = (s === 'approved') ? 'approved' : (s === 'rejected' ? 'rejected' : 'pending');

            tr.innerHTML = `
                <td>
                    <div class="reviewer-cell">
                        <div class="admin-avatar" style="background-color: #3b82f6; width:32px; height:32px; font-size: 0.8rem;">${review.name.charAt(0).toUpperCase()}</div>
                        <span class="reviewer-name">${review.name}</span>
                    </div>
                </td>
                <td class="property-cell">${review.property}</td>
                <td class="rating-cell">
                    <div class="stars">${starsHtml}</div>
                </td>
                <td class="snippet-cell">
                    <div class="snippet-text">${review.comment}</div>
                </td>
                <td class="date-cell">${review.date}</td>
                <td>
                    <span class="status-badge ${statusClass}">${review.status}</span>
                </td>
                <td class="actions-cell">
                    <button class="action-btn edit" data-id="${review.id}" title="Edit Review">
                        <i class="fa-solid fa-pen"></i>
                    </button>
                    <button class="action-btn delete" data-id="${review.id}" title="Delete Review">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            `;
            tableBody.appendChild(tr);
        });

        attachActionListeners();
    }

    // Attach listeners to dynamic buttons
    function attachActionListeners() {
        document.querySelectorAll('.action-btn.delete').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.dataset.id);
                // Instead of native confirm, we could use a custom modal, but simple confirm is fine here
                if(confirm("Are you sure you want to delete this review?")) {
                    fetch(`/api/reviews/${id}`, { method: 'DELETE' })
                        .then(res => {
                            if (res.ok) {
                                const row = document.querySelector(`tr[data-id="${id}"]`);
                                row.classList.add('delete-anim');
                                setTimeout(() => {
                                    const index = reviewsData.findIndex(r => r.id === id);
                                    if (index > -1) {
                                        reviewsData.splice(index, 1);
                                        filterAndRender();
                                    }
                                }, 300);
                            } else {
                                alert("Failed to delete review");
                            }
                        });
                }
            });
        });

        document.querySelectorAll('.action-btn.edit').forEach(btn => {
            btn.addEventListener('click', function() {
                const id = parseInt(this.dataset.id);
                const review = reviewsData.find(r => r.id === id);
                if(review) {
                    currentEditingId = id;
                    editText.value = review.comment;
                    editStatus.value = review.status;
                    editModal.classList.add('active');
                }
            });
        });
    }

    // Filter Logic
    function filterAndRender() {
        const query = searchInput.value.toLowerCase();
        const rating = ratingFilter.value;

        const filtered = reviewsData.filter(r => {
            const matchesQuery = r.name.toLowerCase().includes(query) || 
                                 r.property.toLowerCase().includes(query) || 
                                 r.comment.toLowerCase().includes(query);
            const matchesRating = rating === 'all' || r.rating.toString() === rating;
            
            return matchesQuery && matchesRating;
        });

        renderTable(filtered);
    }

    searchInput.addEventListener('input', filterAndRender);
    ratingFilter.addEventListener('change', filterAndRender);

    // Modal Logic
    closeModalBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            editModal.classList.remove('active');
        });
    });

    editForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const review = reviewsData.find(r => r.id === currentEditingId);
        if(review) {
            const updatedReview = {
                ...review,
                comment: editText.value,
                status: editStatus.value
            };
            
            fetch(`/api/reviews/${currentEditingId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedReview)
            }).then(res => res.json())
              .then(data => {
                  review.comment = data.comment;
                  review.status = data.status;
                  filterAndRender();
                  editModal.classList.remove('active');
              })
              .catch(err => {
                  console.error(err);
                  alert("Failed to update review.");
              });
        }
    });

    // Mobile Sidebar Toggle
    const mobileBtn = document.getElementById('mobile-menu-btn');
    const sidebar = document.querySelector('.sidebar');
    if(mobileBtn) {
        mobileBtn.addEventListener('click', () => {
            sidebar.classList.toggle('active');
        });
    }

    // Initial render
    fetch('/api/reviews')
        .then(res => res.json())
        .then(data => {
            reviewsData = data;
            renderTable(reviewsData);
        })
        .catch(err => console.error("Error fetching reviews:", err));
});
