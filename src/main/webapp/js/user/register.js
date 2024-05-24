document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("RegisterForm");


    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent the default form submission

        if(!form.checkValidity()){
            form.reportValidity();
            return;
        }

        // Get the form data
        const name = document.getElementById("firstName").value;
        const surname = document.getElementById("lastName").value;
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const date = document.getElementById("date").value;
        const gender = document.getElementById("gender").value.charAt(0);

        const user = {
            name: name,
            surname: surname,
            birthday: date,
            gender: gender,
            username: username,
            password: password
        };

        console.log(JSON.stringify(user));
        // Make the API call
        fetch("http://localhost:8080/cycleK-1.0.0/rest/user/signup", {
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
                window.location.href = "login.html";
            })
            .catch(error => {
                alert("Registration failed!");
                console.error("Error:", error);
            });
    });

    // Initial check to disable submit button if fields are not filled
    checkFormFields();
});

