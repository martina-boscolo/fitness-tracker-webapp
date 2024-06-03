document.addEventListener('DOMContentLoaded', function() {
    if (!checkAuth()) {
        logOut()
    }
});

async function fetchExercisePlans() {
    try {
        const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/idUser');
        const exercise_plan = await response.json();
        displayExercisePlans(exercise_plan);
    } catch (error) {
        console.error('Error fetching exercise plans:', error);
    }
}

function displayExercisePlans(exercise_plan) {
    const resource_list = exercise_plan['resource-list'];
    const exercisePlanList = document.getElementById('exercise_plan-list');
    exercisePlanList.innerHTML = ''; // Clear any existing items
    console.log(exercisePlanList)

    resource_list.forEach(plan => {
        const planData = plan.exercise_plan; // Extract plan data

        const listItem = document.createElement('li');

        // Create elements for name and plan
        const nameElement = document.createElement('h3');
        nameElement.textContent = planData.planName;

        const planElement = document.createElement('pre');
        planElement.textContent = JSON.stringify(planData.plan, null, 2); // Display plan as formatted JSON


        // Create Update and Delete buttons
        const updateButton = document.createElement('button');
        updateButton.textContent = 'Update';
        updateButton.className = 'btn btn-primary';
        updateButton.dataset.bsToggle = 'modal';
        updateButton.dataset.bsTarget = '#submitUpdatedExercisePlan';
        updateButton.onclick = () => openUpdateModal(planData);


        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Delete';
        deleteButton.className = 'btn btn-danger';
        deleteButton.onclick = () => deleteExercisePlan(planData);

        // Append name, plan, and buttons to the list item
        listItem.appendChild(nameElement);
        listItem.appendChild(planElement);
        listItem.appendChild(updateButton);
        listItem.appendChild(deleteButton);

        // Append the list item to the exercise plan list
        exercisePlanList.appendChild(listItem);
    });
}


function openUpdateModal(planData) {
    // Open the modal
    const updateModal = new bootstrap.Modal(document.getElementById('addExercisePlanModal'));
    updateModal.show();

    // Pre-fill the form with plan data
    document.getElementById('planName').value = planData.planName;

    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    days.forEach(day => {
        const dayInputs = document.getElementById(day + 'Inputs');
        dayInputs.innerHTML = ''; // Clear existing inputs
        planData.plan[day].forEach(exercise => {
            const input = document.createElement('input');
            input.type = 'text';
            input.className = 'form-control mb-2';
            input.value = exercise;
            dayInputs.appendChild(input);
        });
    });

    // Update form submission to handle update
    const submitButton = document.getElementById('submitNewExercisePlan');
    console.log(submitButton)
    submitButton.textContent = 'Update Exercise Plan';
    submitButton.onclick = () => submitUpdatedExercisePlan(planData.id);
}

async function submitUpdatedExercisePlan(planId) {

    const planName = document.getElementById('planName').value;

    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    const exercisePlan = {};

    days.forEach(day => {
        const inputs = document.querySelectorAll(`#${day}Inputs input`);
        exercisePlan[day] = Array.from(inputs).map(input => input.value);
    });

    const updatedExercisePlan = {
        id: planId,
        idUser: -1,
        planName: planName,
        plan: exercisePlan
    };
    console.log(updatedExercisePlan)

    try {
        const response = await fetch(`http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedExercisePlan)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const responseData = await response.json();
        console.log('Exercise plan updated:', responseData);

        // Optionally, refresh the displayed list of exercise plans
        fetchExercisePlans();

        // Close the modal
        document.getElementById('closeNewExercisePlan').click();

    } catch (error) {
        console.error('Error updating exercise plan:', error);
    }
}


async function deleteExercisePlan(planData) {
    try {
        const response = await fetch(`http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/${planData.id}`, {
            method: 'DELETE',
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        console.log('Exercise plan deleted:', planData.id);

        // Refresh the list of exercise plans
        fetchExercisePlans();

    } catch (error) {
        console.error('Error deleting exercise plan:', error);
    }
}


fetchExercisePlans();