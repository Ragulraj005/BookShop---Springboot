document.addEventListener('DOMContentLoaded', () => {
    const bookForm = document.getElementById('bookForm'); // The form from index.html
    const booksList = document.getElementById('booksList');
    const loadingSpinner = document.getElementById('loading');
    const messageContainer = document.getElementById('message');

    // Helper function to display messages to the user
    function displayMessage(text, type) {
        messageContainer.innerHTML = `<p class="${type}-message">${text}</p>`;
        messageContainer.style.display = 'block';
        setTimeout(() => {
            messageContainer.innerHTML = '';
            messageContainer.style.display = 'none';
        }, 5000); // Message disappears after 5 seconds
    }

    // --- Book Form Submission (Add & Update) ---
    if (bookForm) {
        bookForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevent default form submission

            loadingSpinner.style.display = 'block'; // Show spinner

            // Check if we are in update mode (by checking for a hidden ID field)
            const hiddenIdInput = document.getElementById('bookIdToEdit');
            const isUpdate = hiddenIdInput && hiddenIdInput.value;

            // Get form data
            const bookName = document.getElementById('bookName').value;
            const authorName = document.getElementById('authorName').value;
            const genre = document.getElementById('genre').value;
            const publishedYear = document.getElementById('publishedYear').value;
            const quantity = document.getElementById('quantity').value;

            // Basic client-side validation (optional, but good practice)
            if (!bookName || !authorName || !genre) {
                displayMessage('ERROR: Book Name, Author, and Genre are required.', 'error');
                loadingSpinner.style.display = 'none';
                return;
            }

            const bookData = {
                bookName: bookName,
                authorName: authorName,
                genre: genre,
                // Parse year and quantity to integer, handle empty/invalid values
                publishedYear: publishedYear ? parseInt(publishedYear) : null,
                quantity: quantity ? parseInt(quantity) : 0
            };

            let method = 'POST'; // Default to POST for adding
            let url = '/api/books'; // Default URL for adding

            if (isUpdate) {
                bookData.id = parseInt(hiddenIdInput.value); // Include the ID for update
                method = 'PUT'; // Change method to PUT for updating
                url = `/api/books/${bookData.id}`; // URL for updating specific book
            }

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(bookData)
                });

                loadingSpinner.style.display = 'none'; // Hide spinner

                if (response.ok) {
                    displayMessage(`Book ${isUpdate ? 'updated' : 'added'} successfully!`, 'success');
                    bookForm.reset(); // Clear form fields

                    if (isUpdate) {
                        // If it was an update, reset the form to "add" mode
                        hiddenIdInput.remove(); // Remove the hidden ID field
                        const submitButton = bookForm.querySelector('button[type="submit"]');
                        submitButton.textContent = 'ENTER'; // Reset button text
                    }
                    loadBooks(); // Reload books list to show changes
                } else {
                    const errorText = await response.text();
                    displayMessage(errorText || `Failed to ${isUpdate ? 'update' : 'add'} book.`, 'error');
                    console.error(`Failed to ${isUpdate ? 'update' : 'add'} book:`, response.status, errorText);
                }
            } catch (error) {
                loadingSpinner.style.display = 'none'; // Hide spinner
                console.error('Network or unexpected error during book submission:', error);
                displayMessage('An unexpected error occurred. Please check your network connection.', 'error');
            }
        });
    } else {
        console.error('Book form with ID "bookForm" not found!');
    }


    // --- Function to Load and Display Books ---
    async function loadBooks() {
        booksList.innerHTML = ''; // Clear existing list
        loadingSpinner.style.display = 'block'; // Show spinner

        try {
            const response = await fetch('/api/books'); // Fetch all books
            loadingSpinner.style.display = 'none'; // Hide spinner

            if (response.ok) {
                const books = await response.json();
                if (books.length === 0) {
                    booksList.innerHTML = '<p class="empty-state-message">NO_DATA // INPUT_REQUIRED</p>';
                } else {
                    books.forEach(book => {
                        const bookItem = document.createElement('div');
                        bookItem.classList.add('book-item');
                        // Use book.id for data attributes on buttons
                        bookItem.innerHTML = `
                            <h3>${book.bookName}</h3>
                            <p>Author: ${book.authorName}</p>
                            <p>Genre: ${book.genre}</p>
                            <p>Year: ${book.publishedYear || 'N/A'}</p>
                            <p>Quantity: ${book.quantity || 0}</p>
                            <div class="book-actions">
                                <button class="btn-edit" data-id="${book.id}">EDIT</button>
                                <button class="btn-delete" data-id="${book.id}">DELETE</button>
                            </div>
                        `;
                        booksList.appendChild(bookItem);
                    });
                    // After adding all books, attach event listeners to their buttons
                    attachBookActionListeners();
                }
            } else {
                displayMessage('Failed to load books.', 'error');
                console.error('Failed to load books:', response.status);
                booksList.innerHTML = '<p class="error-message">ERROR // FAILED_TO_LOAD_DATA</p>';
            }
        } catch (error) {
            loadingSpinner.style.display = 'none'; // Hide spinner
            console.error('Network or unexpected error during book loading:', error);
            displayMessage('An unexpected error occurred while loading books.', 'error');
            booksList.innerHTML = '<p class="error-message">ERROR // NETWORK_ISSUE</p>';
        }
    }

    // --- Function to Attach Event Listeners to Dynamically Created Buttons ---
    function attachBookActionListeners() {
        // --- Delete Button Logic ---
        document.querySelectorAll('.btn-delete').forEach(button => {
            button.addEventListener('click', async (event) => {
                const bookId = event.target.dataset.id; // Get book ID from data-id attribute
                if (confirm(`CONFIRMATION: Delete Book ID ${bookId}?`)) { // User confirmation
                    loadingSpinner.style.display = 'block'; // Show spinner
                    try {
                        const response = await fetch(`/api/books/${bookId}`, {
                            method: 'DELETE',
                        });
                        loadingSpinner.style.display = 'none'; // Hide spinner
                        if (response.ok) {
                            displayMessage('Book DELETED successfully!', 'success');
                            loadBooks(); // Reload the list after deletion
                        } else {
                            const errorText = await response.text();
                            displayMessage(errorText || 'ERROR: Failed to delete book.', 'error');
                            console.error('Failed to delete book:', response.status, errorText);
                        }
                    } catch (error) {
                        loadingSpinner.style.display = 'none'; // Hide spinner
                        console.error('Network or unexpected error during book deletion:', error);
                        displayMessage('ERROR: Network issue during deletion.', 'error');
                    }
                }
            });
        });

        // --- Edit Button Logic ---
        document.querySelectorAll('.btn-edit').forEach(button => {
            button.addEventListener('click', async (event) => {
                const bookId = event.target.dataset.id; // Get book ID from data-id attribute
                loadingSpinner.style.display = 'block'; // Show spinner
                try {
                    const response = await fetch(`/api/books/${bookId}`); // Fetch book details by ID
                    loadingSpinner.style.display = 'none'; // Hide spinner
                    if (response.ok) {
                        const bookToEdit = await response.json();

                        // Populate the form fields with the fetched book data
                        document.getElementById('bookName').value = bookToEdit.bookName;
                        document.getElementById('authorName').value = bookToEdit.authorName;
                        document.getElementById('genre').value = bookToEdit.genre;
                        document.getElementById('publishedYear').value = bookToEdit.publishedYear || ''; // Handle null
                        document.getElementById('quantity').value = bookToEdit.quantity || 0; // Handle null

                        // Create or update a hidden input field to store the ID of the book being edited
                        let hiddenIdInput = document.getElementById('bookIdToEdit');
                        if (!hiddenIdInput) {
                            hiddenIdInput = document.createElement('input');
                            hiddenIdInput.type = 'hidden';
                            hiddenIdInput.id = 'bookIdToEdit';
                            hiddenIdInput.name = 'id'; // Name it 'id' if your backend expects it for updates
                            bookForm.appendChild(hiddenIdInput); // Add to the form
                        }
                        hiddenIdInput.value = bookToEdit.id;

                        // Change the submit button text to indicate update mode
                        const submitButton = bookForm.querySelector('button[type="submit"]');
                        submitButton.textContent = 'UPDATE RECORD';
                        displayMessage('EDIT MODE: Modify and click UPDATE RECORD.', 'info');

                        // Scroll to the form section for convenience
                        window.scrollTo({ top: 0, behavior: 'smooth' });

                    } else {
                        displayMessage('ERROR: Failed to fetch book for editing.', 'error');
                        console.error('Failed to fetch book for editing:', response.status);
                    }
                } catch (error) {
                    loadingSpinner.style.display = 'none'; // Hide spinner
                    console.error('Network or unexpected error during fetching book for edit:', error);
                    displayMessage('ERROR: Network issue while fetching data for edit.', 'error');
                }
            });
        });
    }

    // Initial load of books when the page loads
    loadBooks();

    // Make loadBooks globally accessible for the refresh button (onclick)
    window.loadBooks = loadBooks;
});