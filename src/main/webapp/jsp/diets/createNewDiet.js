function addFoodInput(containerId) {
    const container = document.getElementById(containerId);

    const foodInput = document.createElement('input');
    foodInput.type = 'text';
    foodInput.placeholder = 'Food';
    foodInput.className = 'form-control mb-2';
    foodInput.name = `${containerId}Food`;

    const qtyInput = document.createElement('input');
    qtyInput.type = 'number';
    qtyInput.placeholder = 'Quantity (grams)';
    qtyInput.className = 'form-control mb-2';
    qtyInput.name = `${containerId}Qty`;

    const selectInput = document.createElement('select');
    selectInput.className = 'form-select mb-2';
    selectInput.name = `${containerId}Meal`;
    selectInput.innerHTML = `
                <option value="Breakfast">Breakfast</option>
                <option value="Lunch">Lunch</option>
                <option value="Dinner">Dinner</option>
            `;

    const row = document.createElement('div');
    row.className = 'row align-items-center';

    const col1 = document.createElement('div');
    col1.className = 'col';
    col1.appendChild(foodInput);

    const col2 = document.createElement('div');
    col2.className = 'col';
    col2.appendChild(qtyInput);

    const col3 = document.createElement('div');
    col3.className = 'col';
    col3.appendChild(selectInput);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);

    container.appendChild(row);
}

document.getElementById('addDietForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Impedisce il comportamento predefinito del form

    const idUser = 1; // Imposta un valore fisso per l'idUser
    const planName = document.getElementById('planName').value;

    function getMealData(day) {
        const dayInputs = document.getElementById(day);
        const rows = dayInputs.getElementsByClassName('row');
        const meals = { Breakfast: {}, Lunch: {}, Dinner: {} };


        let breakfastCount = 0;
        let lunchCount = 0;
        let dinnerCount = 0;

        Array.from(rows).forEach(row => {
            const food = row.children[0].value;
            const quantity = parseInt(row.children[1].value);
            const mealType = row.children[2].value;
            if (food && !isNaN(quantity)) {
                meals[mealType][food] = quantity;
                if (mealType === 'Breakfast') breakfastCount++;
                if (mealType === 'Lunch') lunchCount++;
                if (mealType === 'Dinner') dinnerCount++;
            }
        });


        if (breakfastCount === 0) meals.Breakfast = { "No food provided": "" };
        if (lunchCount === 0) meals.Lunch = { "No food provided": "" };
        if (dinnerCount === 0) meals.Dinner = { "No food provided": "" };

        return meals;
    }

    const dietPlan = {
        idUser: idUser,
        planName: planName,
        diet: {
            Monday: getMealData('mondayInputs'),
            Tuesday: getMealData('tuesdayInputs'),
            Wednesday: getMealData('wednesdayInputs'),
            Thursday: getMealData('thursdayInputs'),
            Friday: getMealData('fridayInputs'),
            Saturday: getMealData('saturdayInputs'),
            Sunday: getMealData('sundayInputs')
        }
    };

    fetch('http://localhost:8080/cycleK-1.0.0/rest/diet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dietPlan)
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