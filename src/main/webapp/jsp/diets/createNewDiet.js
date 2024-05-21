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
        console.log(newDiet)
    });


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


    notificationContainer.appendChild(alertDiv);


    setTimeout(() => {
        $(alertDiv).alert('close');
    }, 3000);
}

const createNewDietPlanBtn = document.getElementById('createNewDietPlanBtn');
const addDietModal = document.getElementById('addDietModal');
createNewDietPlanBtn.addEventListener('click', function () {
    addDietModal.style.display = 'block';
});