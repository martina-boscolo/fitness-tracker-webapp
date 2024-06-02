document.getElementById('submitNewExercisePlan').addEventListener('click', async function () {
        const planName = document.getElementById('planName').value;

        const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
        const exercisePlan = {};

        days.forEach(day => {
            const inputs = document.querySelectorAll(`#${day}Inputs input`);
            exercisePlan[day] = Array.from(inputs).map(input => input.value);
        });

        const newExercisePlan = {
            planName: planName,
            plan: exercisePlan
        };

        try {
            const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercise_plan', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newExercisePlan)
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const responseData = await response.json;
            console.log('New exercise plan added:', responseData);

            // Optionally, refresh the displayed list of exercise plans
            fetchExercisePlans();

            // Close the modal
            document.getElementById('closeNewExercisePlan').click();

        } catch (error) {
            console.error('Error adding new exercise plan:', error);
        }
    });

    function addExerciseInput(containerId) {
        const container = document.getElementById(containerId);

        const ExerciseInput = document.createElement('input');
        ExerciseInput.type = 'text';
        ExerciseInput.placeholder = 'Exercise';
        ExerciseInput.className = 'form-control mb-2';
        ExerciseInput.name = `${containerId}ExerciseName`;

        const repeatInput = document.createElement('input');
        repeatInput.type = 'number';
        repeatInput.placeholder = 'repetition';
        repeatInput.className = 'form-control mb-2';
        repeatInput.name = `${containerId}repeat`;

        const setsInput = document.createElement('input');
        setsInput.type = 'number';
        setsInput.placeholder = 'sets';
        setsInput.className = 'form-control mb-2';
        setsInput.name = `${containerId}sets`;


        const weightInput = document.createElement('input');
        weightInput.type = 'number';
        weightInput.placeholder = 'weight';
        weightInput.className = 'form-control mb-2';
        weightInput.name = `${containerId}weights`;

        const deleteExerciseButton = document.createElement('button');
        deleteExerciseButton.type = 'button';
        deleteExerciseButton.className = 'btn btn-danger mb-2';

        const row = document.createElement('div');
        row.className = 'row align-items-center';

        const col1 = document.createElement('div');
        col1.className = 'col';
        col1.appendChild(ExerciseInput);

        const col2 = document.createElement('div');
        col2.className = 'col';
        col2.appendChild(repeatInput);

        const col3 = document.createElement('div');
        col3.className = 'col';
        col3.appendChild(setsInput);

        const col4 = document.createElement('div');
        col4.className = 'col';
        col4.appendChild(weightInput);

        const col5 = document.createElement('div');
        col5.className = 'col-auto';
        col5.appendChild(deleteExerciseButton);

        const trashIcon = document.createElement('i');
        trashIcon.className = 'bi bi-trash3';
        deleteExerciseButton.appendChild(trashIcon);

        row.appendChild(col1);
        row.appendChild(col2);
        row.appendChild(col3);
        row.appendChild(col4);
        row.appendChild(col5);

        container.appendChild(row);

        deleteExerciseButton.addEventListener('click', () => {
            container.removeChild(row);
        });
    }

    const createNewExercisePlanBtn = document.getElementById('submitNewExercisePlan');
    const addExerciseModal = document.getElementById('addExerciseModal');
    createNewExercisePlanBtn.addEventListener('click', function () {
        addExerciseModal.style.display = 'block';
    });

