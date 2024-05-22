function populateForm() {
    fetch("http://localhost:8080/cycleK-1.0.0/rest/user/id/1") //modify with token
        .then(response => response.json())
        .then(data => {
            const user = data.user;
            document.getElementById("firstName").value = user.name || '';
            document.getElementById("lastName").value = user.surname || '';
            document.getElementById("date").value = user.birthday || '';
            document.getElementById("gender").value = mapGender(user.gender) || '';
            document.getElementById("username").value = user.username || '';
            document.getElementById("password").value = user.password || '';

            // Store the initial form state
            storeInitialFormState();
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

function returnDefault() {
    location.reload();
}

let initialFormState = {};

function storeInitialFormState() {
    initialFormState = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        date: document.getElementById("date").value,
        gender: document.getElementById("gender").value,
    };
}

function checkFormChanges() {
    const currentFormState = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        date: document.getElementById("date").value,
        gender: document.getElementById("gender").value,
    };

    const hasChanged = Object.keys(initialFormState).some(key => initialFormState[key] !== currentFormState[key]);
    document.getElementById("changeButton").disabled = !hasChanged;
}

document.addEventListener("DOMContentLoaded", function () {
    populateForm();

    document.getElementById("changeButton").disabled = true;
    // Add event listeners to all form inputs to detect changes
    const formInputs = document.querySelectorAll("#ProfileForm input");
    formInputs.forEach(input => {
        input.addEventListener("input", checkFormChanges);
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
        fetch("http://localhost:8080/cycleK-1.0.0/rest/user/id/1", {
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
                location.reload();

            })
            .catch(error => {
                alert("registration failed!");
                console.error("Error:", error);
            });
    });
});