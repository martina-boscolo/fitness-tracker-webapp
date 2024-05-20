function populateForm() {
    fetch("http://localhost:8080/cycleK_war_exploded/rest/user/id/1") //modify with token
        .then(response => response.json())
        .then(data => {
            const user = data.user;
            document.getElementById("firstName").value = user.name || '';
            document.getElementById("lastName").value = user.surname || '';
            document.getElementById("date").value = user.birthday || '';
            document.getElementById("gender").value = mapGender(user.gender) || '';
            document.getElementById("username").value = user.username || '';
            document.getElementById("password").value = user.password || '';
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

function mapGender(gender) {
    switch (gender) {
        case "M":
            return "Male";
        case "F":
            return "Female";
        case "O":
            return "Other";
        default:
            return "";
    }
}

function enableChangeButton() {
    const formInputs = document.querySelectorAll("#ProfileForm input, #ProfileForm select");
    let isModified = false;

    formInputs.forEach(input => {
        if (input.value !== input.defaultValue) {
            isModified = true;
        }
    });

    document.getElementById("changeButton").disabled = !isModified;
}

document.addEventListener("DOMContentLoaded", function () {
    populateForm();
    enableChangeButton()

    const formInputs = document.querySelectorAll("#ProfileForm input, #ProfileForm select");
    formInputs.forEach(input => {
        input.addEventListener("change", enableChangeButton);
    });

    document.getElementById("ProfileForm").addEventListener("submit", function (event) {
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
        fetch("http://localhost:8080/cycleK_war_exploded/rest/user/id/1", {
            method: "PUT",
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
});