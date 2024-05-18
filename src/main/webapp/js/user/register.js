document.getElementById("RegisterForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission

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

    console.log(JSON.stringify(user))
    // Make the API call
    fetch("http://localhost:8080/cycleK_war_exploded/rest/user/signup", {
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

        })
        .catch(error => {
            alert("registration failed!");
            console.error("Error:", error);
        });
});

