// Gestisci l'invio del modulo
document.getElementById('addDietForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const planName = document.getElementById('planName').value;
    const newDiet = {
        idUser: 1,
        planName: planName,
        diet: {}
    };

    const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

    daysOfWeek.forEach(day => {
        const dayMeals = {};
        const dayInputs = document.querySelectorAll(`#${day.toLowerCase()}Inputs .row`);

        dayInputs.forEach(row => {
            const food = row.querySelector('input[placeholder="Food"]').value;
            const quantity = row.querySelector('input[placeholder="Quantity (grams)"]').value;
            const mealType = row.querySelector('select').value;

            if (!dayMeals[mealType]) {
                dayMeals[mealType] = {};
            }

            if (!isNaN(quantity) && quantity !== '') {
                if (food) {
                    dayMeals[mealType][food] = parseInt(quantity, 10);
                }
            }
        });

        newDiet.diet[day] = dayMeals;
    });

    // Invia il nuovo piano alimentare al backend
    fetch('http://localhost:8080/cycleK-1.0.0/rest/diet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newDiet)
    })
        .then(response => response.json())
        .then(addedDiet => {
            console.log('Piano alimentare aggiunto:', addedDiet);

            // Aggiorna i dati locali e visualizza il nuovo piano alimentare
            data['resource-list'].push({ diet: addedDiet });
            displayDietPlan(data['resource-list'].length - 1); // Mostra il nuovo piano alimentare aggiunto
            showNotification('Piano alimentare aggiunto con successo!', 'success');
        })
        .catch(error => console.error('Errore:', error));
});

function addFoodInput(containerId) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error(`Container element with ID '${containerId}' not found.`);
        return;
    }
    const newFoodInput = `
                <div class="row mt-2">
                    <div class="col">
                        <input type="text" class="form-control" placeholder="Food">
                    </div>
                    <div class="col">
                        <input type="number" class="form-control" placeholder="Quantity (grams)">
                    </div>
                    <div class="col">
                        <select class="form-select">
                            <option value="Breakfast">Breakfast</option>
                            <option value="Lunch">Lunch</option>
                            <option value="Dinner">Dinner</option>
                        </select>
                    </div>
                </div>
            `;
    container.insertAdjacentHTML('beforeend', newFoodInput);
}

function showNotification(message, type) {
    const notificationContainer = document.getElementById('notificationContainer');

    // Create a new alert element
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            `;

    // Add the element to the notification container
    notificationContainer.appendChild(alertDiv);

    // Automatically remove the notification after 3 seconds
    setTimeout(() => {
        $(alertDiv).alert('close');
    }, 3000);
}

document.querySelector('[onclick="addFoodInput(\'mondayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('mondayInputs');
});
document.querySelector('[onclick="addFoodInput(\'tuesdayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('tuesdayInputs');
});
document.querySelector('[onclick="addFoodInput(\'wednesdayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('wednesdayInputs');
});
document.querySelector('[onclick="addFoodInput(\'thursdayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('thursdayInputs');
});
document.querySelector('[onclick="addFoodInput(\'fridayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('fridayInputs');
});
document.querySelector('[onclick="addFoodInput(\'saturdayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('saturdayInputs');
});
document.querySelector('[onclick="addFoodInput(\'sundayInputs\')"]').addEventListener('click', function(event) {
    event.preventDefault();
    addFoodInput('sundayInputs');
});

const createNewDietPlanBtn = document.getElementById('createNewDietPlanBtn');
const addDietFormContainer = document.getElementById('addDietFormContainer');
createNewDietPlanBtn.addEventListener('click', function () {
    addDietFormContainer.style.display = 'block';
});