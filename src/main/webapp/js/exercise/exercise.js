document.addEventListener('DOMContentLoaded', function() {
    if (!checkAuth()) {
        logOut()
    }
});

async function fetchExercises() {
    try {
        const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercises');
        const exercises = await response.json();
        displayExercises(exercises);
    } catch (error) {
        console.error('Error fetching exercises:', error);
    }
}

function displayExercises(exercises) {
    const resource_list = exercises['resource-list'];
    const exerciseList = document.getElementById('exercise-list');
    exerciseList.innerHTML = ''; // Clear any existing items
    resource_list.forEach(exercise => {
        exercise = exercise.exercise
        // const listItem = document.createElement('li');
        // listItem.textContent = exercise.exercise_name;  // Adjust based on your exercise object structure
        const listItem = document.createElement('li');

        // Create elements for name and description
        const nameElement = document.createElement('h3');
        nameElement.textContent = exercise.exercise_name;

        const descriptionElement = document.createElement('p');
        descriptionElement.textContent = exercise.description;

        // Append name and description to the list item
        listItem.appendChild(nameElement);
        listItem.appendChild(descriptionElement);

        // Append the list item to the exercise list
        exerciseList.appendChild(listItem);
        // exerciseList.appendChild(listItem);
        console.log(exercise.exercise_name, exercise.description);
    });
}

fetchExercises();
