document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission

    // Get the form data
    const username = document.getElementById("InputUsername").value;
    const password = document.getElementById("InputPassword").value;

    const user = {
        username: username,
        password: password
    };

    console.log(JSON.stringify(user))
    // Make the API call
    fetch("http://localhost:8080/cycleK-1.0.0/rest/user/login", {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json())
        .then(data => {
            // Handle the response data
            console.log("Success:", data);
            const token = data.token;
            console.log(token)
            if (token) {
                setCookie('authToken', token, 120); // Set the token as a cookie
                window.location.href = "html/stats.html";
                // Optionally, redirect the user or perform other actions
            } else {
                alert("Login failed: Token not found in response.");
            }
            // Optionally, redirect the user or perform other actions
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Login failed!");
        });
});

function setCookie(name, value, minutes, path = '/', domain = window.location.hostname, secure = true) {
    let cookie = `${name}=${encodeURIComponent(value)};`;

    if (minutes) {
        const date = new Date();
        date.setTime(date.getTime() + (minutes * 60 * 1000));
        cookie += `expires=${date.toUTCString()};`;
    }

    cookie += `path=${path};`;

    if (domain) {
        cookie += `domain=${domain};`;
    }

    if (secure) {
        cookie += `secure;`;
    }

    document.cookie = cookie;
}
