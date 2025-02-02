document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission

    // Get the form data
    const username = document.getElementById("InputUsername").value;
    const password = document.getElementById("InputPassword").value;

    const user = {
        username: username,
        password: password
    };
    // Make the API call
    fetch("http://localhost:8080/cycleK-1.0.0/rest/user/login", {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                resetForm();
                renderError(document.getElementById("InputUsername"));
                renderError(document.getElementById("InputPassword"));
                document.getElementById("login-error").style.visibility ="visible";
            }
            return response.json();
        })
        .then(data => {
            // Handle the response data
            const token = data.token;
            if (token) {
                logIn(token);
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
});

function resetForm() {
    const form = document.getElementById('loginForm'); // Assuming your form has an id of 'loginForm'
    form.reset();
}

function renderError(input) {
    if (input) {
        input.classList.add("error"); // Add CSS class to render username input in red
    }
}

document.getElementById("InputUsername").addEventListener("input", handleInput);
document.getElementById("InputPassword").addEventListener("input", handleInput);

function handleInput() {
    document.getElementById("login-error").style.visibility = "hidden";
    const inputs = document.querySelectorAll("#InputUsername, #InputPassword");
    inputs.forEach(input => {
        input.classList.remove("error");
    });
}