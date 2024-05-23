function openEditForm() {
    getCurrentDietData(); // Call the function to fetch and populate the form

    // Open the edit modal
    var editModal = new bootstrap.Modal(document.getElementById('editDietModal'));
    editModal.show();
}

function getCurrentDietData() {
    // Fetch the current diet data from the backend
    fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/' + dietId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error fetching current diet data');
            }
            return response.json();
        })
        .then(data => {
            // Populate the form fields with the current diet data
            document.getElementById("planName").value = data.planName;
            // Add logic to populate other form fields based on the data received
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle errors and provide feedback to the user
        });
}

function updateDiet() {
    // Get the updated diet data from the form
    var updatedDietData = {
        "planName": document.getElementById("planName").value,
        // Add logic to retrieve and update other data fields from the form
    };

    // Send a PUT request to the backend with the updated diet data
    fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/' + dietId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedDietData),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error updating diet');
            }
            return response.json();
        })
        .then(data => {
            console.log('Diet updated successfully:', data);
            // Handle success feedback or other post-update actions
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle errors and provide feedback to the user
        });
}
