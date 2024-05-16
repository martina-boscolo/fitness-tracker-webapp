document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the default form submission

    // Get the form data
    const username = document.getElementById('InputUsername').value;
    const password = document.getElementById('InputPassword').value;

    const user = {
        username: username,
        password: password
    };

    // Make the API call
    fetch('http://localhost:8080/cycleK-1.0.0/rest/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json())
        .then(data => {
            // Handle the response data
            console.log('Success:', data);
            alert('Login successful!');
            // Optionally, redirect the user or perform other actions
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Login failed!');
        });
});
