// src/main/resources/static/js/login.js
document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signupForm'); // This is the signup form
    const messageContainer = document.getElementById('message');

    function displayMessage(text, type) {
        messageContainer.innerHTML = `<p class="<span class="math-inline">\{type\}\-message"\></span>{text}</p>`;
        messageContainer.style.display = 'block';
        setTimeout(() => {
            messageContainer.innerHTML = '';
            messageContainer.style.display = 'none';
        }, 5000);
    }

    if (signupForm) {
        signupForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            if (!username || !password) {
                displayMessage('ERROR: Username and Password are required.', 'error');
                return;
            }

            const userData = {
                username: username,
                password: password
            };

            try {
                const response = await fetch('/req/signup', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(userData)
                });

                if (response.ok) {
                    const message = await response.text();
                    displayMessage(message || 'Registration successful! Redirecting to login.', 'success');
                    signupForm.reset();
                    setTimeout(() => {
                        window.location.href = '/req/login';
                    }, 2000);
                } else {
                    const errorText = await response.text();
                    displayMessage(errorText || 'Registration failed. Please try again.', 'error');
                    console.error('Registration failed:', response.status, errorText);
                }
            } catch (error) {
                console.error('Network or unexpected error during registration:', error);
                displayMessage('An unexpected error occurred. Please check your internet connection.', 'error');
            }
        });
    } else {
        // This else block is now okay for login.html if signupForm is for a separate signup section
        // console.error('Signup form not found!'); // You can remove this specific error now if it's expected
    }

    // --- Add Login form handling here if it's in the same file ---
    const loginForm = document.getElementById('loginForm'); // Assuming your login form has this ID
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            // ... add your login form submission logic here if it's AJAX based ...
            // For form-based login, you just need action="/login" method="post"
            // Spring Security handles the direct form submission.
        });
    }
    // --- End Login form handling ---

});