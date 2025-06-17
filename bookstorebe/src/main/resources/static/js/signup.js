document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signupForm');
    const messageContainer = document.getElementById('message');

    function displayMessage(text, type) {
        messageContainer.innerHTML = `<p class="${type}-message">${text}</p>`;
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
            // REMOVED: const email = document.getElementById('email').value; // This line was removed
            const password = document.getElementById('password').value;

            // Basic client-side validation (UPDATED: 'email' removed from this condition)
            if (!username || !password) {
                displayMessage('ERROR: Username and Password are required.', 'error');
                return;
            }

            const userData = {
                username: username,
                // REMOVED: email: email, // 'email' removed from userData object
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
        console.error('Signup form not found!');
    }
});
