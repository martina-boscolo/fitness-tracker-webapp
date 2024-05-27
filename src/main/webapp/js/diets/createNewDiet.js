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
                <option valueOverlap="Dinner">Dinner</option>
            `;

    const deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.className = 'btn btn-danger mb-2';

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

    const col4 = document.createElement('div');
    col4.className = 'col-auto';
    col4.appendChild(deleteButton);

    const trashIcon = document.createElement('i');
    trashIcon.className = 'bi bi-trash3';
    deleteButton.appendChild(trashIcon);

    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);

    container.appendChild(row);

    deleteButton.addEventListener('click', () => {
        container.removeChild(row);
    });
}

document.getElementById('submitNewDiet').addEventListener('click', (event) => {
    let Cookies = document.cookie;
    if (Cookies.indexOf('authToken') === -1) {
        console.error('Error: Unauthorized - Redirecting to login page.');
        // Redirect to login page
        window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
    } else {
        processNewDiet(event)
    }
});

function processNewDiet(event) {
    event.preventDefault();

    const planName = document.getElementById('planName').value;

    function getMealData(day) {
        const dayInputs = document.getElementById(day);
        const rows = dayInputs.getElementsByClassName('row');
        const meals = {Breakfast: {}, Lunch: {}, Dinner: {}};


        let breakfastCount = 0;
        let lunchCount = 0;
        let dinnerCount = 0;

        Array.from(rows).forEach(row => {
            const food = row.children[0].firstElementChild.value;
            const quantity = parseInt(row.children[1].firstElementChild.value);
            const mealType = row.children[2].firstElementChild.value;
            if (food && !isNaN(quantity)) {
                meals[mealType][food] = quantity;
                if (mealType === 'Breakfast') breakfastCount++;
                if (mealType === 'Lunch') lunchCount++;
                if (mealType === 'Dinner') dinnerCount++;
            }
        });

        if (breakfastCount === 0) meals.Breakfast = {"No food provided": ""};
        if (lunchCount === 0) meals.Lunch = {"No food provided": ""};
        if (dinnerCount === 0) meals.Dinner = {"No food provided": ""};

        return meals;
    }

    const dietPlan = {
        idUser: -1,
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
        .then(response => {
            if (!response.ok) {
                // Check for 401 Unauthorized
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                }
                // Throw an error for other non-success statuses
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(addedDiet => {
            console.log('Piano alimentare aggiunto:', addedDiet);

            data['resource-list'].push({diet: addedDiet});
            displayDietPlan(data['resource-list'].length - 1);
            showNotification('Piano alimentare aggiunto con successo!', 'success');
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });
    $('#addDietModal').modal('hide')
}

const createNewDietPlanBtn = document.getElementById('submitNewDiet');
const addDietModal = document.getElementById('addDietModal');
createNewDietPlanBtn.addEventListener('click', function () {
    addDietModal.style.display = 'block';
});