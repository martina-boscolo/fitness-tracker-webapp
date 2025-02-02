document.addEventListener("DOMContentLoaded", function () {
    if (!checkAuth()) {
        window.location.href = BASE_URL;
    }
});

let idDiet = -1;

fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/idUser/', {
    credentials: 'include'
})
    .then(response => response.json())
    .then(data => {

        let currentPlanIndex = 0; // Keep track of the current displayed diet plan

        // Function to display a specific diet plan
        function displayDietPlan(planIndex) {
            const dietPlan = data['resource-list'][planIndex];
            let planName = dietPlan['diet']['planName'];
            let diet = dietPlan['diet']['diet'];
            idDiet = dietPlan['diet']['id'];
            // Clear existing content except for navigation buttons
            const contentContainer = document.getElementById('content-container');
            contentContainer.innerHTML = '';

            document.getElementById('dietPlanTitle').innerHTML = "Diet Plan: " + planName;

            // Display the plan name and diet date
            const dataContainer = document.createElement('div');
            dataContainer.id = 'content';

            // Create tabs for each day
            const tabList = document.createElement('ul');
            tabList.classList.add('nav', 'nav-tabs');
            tabList.setAttribute('id', `daysTab`);

            const tabContent = document.createElement('div');
            tabContent.classList.add('tab-content');

            // Iterate through the days in the diet
            let isFirstDay = true;
            for (let day in diet) {
                // Create tab pane for the day
                const tabPane = document.createElement('div');
                tabPane.classList.add('tab-pane', 'fade');
                tabPane.setAttribute('id', `day-${day}`);

                // If it's the first day, mark it as active
                if (isFirstDay) {
                    tabPane.classList.add('show', 'active');
                    isFirstDay = false;
                }

                // Create tab link
                const tabLink = document.createElement('a');
                tabLink.classList.add('nav-link');
                tabLink.setAttribute('id', `day-${day}-tab`);
                tabLink.setAttribute('data-bs-toggle', 'tab');
                tabLink.setAttribute('href', `#day-${day}`);
                tabLink.setAttribute('role', 'tab');
                tabLink.setAttribute('aria-controls', `day-${day}`);
                tabLink.setAttribute('aria-selected', isFirstDay.toString()); // Set aria-selected based on isFirstDay

                tabLink.textContent = day;

                // Append tab link to tab list
                const tabListItem = document.createElement('li');
                tabListItem.classList.add('nav-item');
                tabListItem.appendChild(tabLink);
                tabList.appendChild(tabListItem);

                // Iterate through the meals of the day
                let dayContent = '';
                for (let meal in diet[day]) {
                    dayContent += `<div class="meal mb-3">
                                        <h3 class="text-secondary">${meal}</h3>
                                        <ul class="list-group">`;

                    // Iterate through the foods in the meal
                    for (let food in diet[day][meal]) {
                        if (typeof diet[day][meal][food] === 'object') {
                            // If the food item is an object, iterate through its properties
                            dayContent += `<li class="list-group-item">${food}:`;
                            dayContent += `<ul class="list-group list-group-flush">`;
                            for (let subFood in diet[day][meal][food]) {
                                dayContent += `<li class="list-group-item">${subFood}: ${diet[day][meal][food][subFood]} grams</li>`;
                            }
                            dayContent += `</ul></li>`;
                        } else {
                            // If the food item is not an object, display it directly
                            dayContent += `<li class="list-group-item">${food}: ${diet[day][meal][food]} grams</li>`;
                        }
                    }

                    dayContent += `</ul></div>`;
                }

                // Append day content to tab pane
                tabPane.innerHTML = dayContent;

                // Append tab pane to tab content
                tabContent.appendChild(tabPane);
            }

            // Append tab list and tab content to data container
            dataContainer.appendChild(tabList);
            dataContainer.appendChild(tabContent);

            // Append data container to the content container
            contentContainer.appendChild(dataContainer);
        }

        // Display the first diet plan initially
        displayDietPlan(currentPlanIndex);

        // Function to navigate to the next diet plan
        function nextDietPlan() {
            currentPlanIndex = (currentPlanIndex + 1) % data['resource-list'].length;
            displayDietPlan(currentPlanIndex);
        }

        // Function to navigate to the previous diet plan
        function previousDietPlan() {
            currentPlanIndex = (currentPlanIndex - 1 + data['resource-list'].length) % data['resource-list'].length;
            displayDietPlan(currentPlanIndex);
        }

        const prevButton = document.getElementById('prevDiet');
        prevButton.addEventListener('click', previousDietPlan);
        prevButton.addEventListener('mouseover', () => {
            prevButton.setAttribute("class", "bi bi-arrow-left-circle-fill")
        });
        prevButton.addEventListener('mouseout', () => {
            prevButton.setAttribute("class", "bi bi-arrow-left-circle")
        });

        const nextButton = document.getElementById('nextDiet');
        nextButton.addEventListener('click', nextDietPlan);
        nextButton.addEventListener('mouseover', () => {
            nextButton.setAttribute("class", "bi bi-arrow-right-circle-fill")
        });
        nextButton.addEventListener('mouseout', () => {
            nextButton.setAttribute("class", "bi bi-arrow-right-circle")
        });

        $('#editDietModal').on('shown.bs.modal', function (e) {
            updateDiet(data['resource-list'][currentPlanIndex]);
        });

    })
    .catch(error => console.error('Error:', error));

function updateDiet(data) {
    const diet = data['diet']['diet'];
    const form = document.getElementById('dietForm');

    // Clear existing form content
    form.innerHTML = '';

    // Input field for plan name
    const planNameInputContainer = document.createElement('div');
    planNameInputContainer.classList.add('mb-3');

    const planNameLabel = document.createElement('label');
    planNameLabel.textContent = 'Plan Name';
    planNameLabel.setAttribute('for', 'planName');
    planNameLabel.classList.add('form-label');
    planNameInputContainer.appendChild(planNameLabel);

    const planNameInput = document.createElement('input');
    planNameInput.type = 'text';
    planNameInput.classList.add('form-control');
    planNameInput.setAttribute('required', 'true');
    // Set the value of the input field to the plan name from the diet data
    planNameInput.value = data['diet']['planName'];
    planNameInput.id = 'planNameId';
    planNameInput.disabled = true;
    planNameInput.classList.add('disabled-input');
    planNameInputContainer.appendChild(planNameInput);
    form.appendChild(planNameInputContainer);


    for (let day in diet) {
        // Create a container for each day's inputs
        const dayContainer = document.createElement('div');
        dayContainer.classList.add('mb-3');

        // Day label
        const dayLabel = document.createElement('label');
        dayLabel.classList.add('form-label');
        dayLabel.textContent = day;
        dayContainer.appendChild(dayLabel);

        const dayInputsContainer = document.createElement('div');
        dayInputsContainer.classList.add('row');
        dayInputsContainer.id = `${day.toLowerCase()}`;

        const addFoodButton = document.createElement('button');
        addFoodButton.textContent = 'Add Food';
        addFoodButton.classList.add('btn', 'custom');
        addFoodButton.setAttribute('type', 'button')
        addFoodButton.onclick = function () {
            addFoodInput(`${day.toLowerCase()}`);
        };

        dayContainer.appendChild(dayInputsContainer);
        dayContainer.appendChild(addFoodButton);

        form.appendChild(dayContainer);

        const foods = diet[day];
        for (let meal in foods) {
            for (let food in foods[meal]) {

                const rowContainer = document.createElement('div')
                rowContainer.classList.add('row', 'align-items-center')

                const foodQuantity = foods[meal][food];

                const col1 = document.createElement('div');
                col1.className = 'col';
                const col2 = document.createElement('div');
                col2.className = 'col';
                const col3 = document.createElement('div');
                col3.className = 'col';
                const col4 = document.createElement('div');
                col4.className = 'col';

                // Food name input
                const foodNameInput = document.createElement('input');
                foodNameInput.type = 'text';
                foodNameInput.classList.add('form-control', 'mb-2');
                foodNameInput.placeholder = 'Food';
                foodNameInput.name = `${day.toLowerCase()}Food`;
                foodNameInput.value = food;
                col1.appendChild(foodNameInput);

                // Food quantity input
                const foodQuantityInput = document.createElement('input');
                foodQuantityInput.type = 'number';
                foodQuantityInput.classList.add('form-control', 'mb-2');
                foodQuantityInput.placeholder = 'Quantity';
                foodQuantityInput.name = `${day.toLowerCase()}Qty`;
                foodQuantityInput.value = foodQuantity;
                col2.appendChild(foodQuantityInput);

                // Meal select input
                const mealSelect = document.createElement('select');
                mealSelect.classList.add('form-control', 'mb-2');
                ['Breakfast', 'Lunch', 'Dinner'].forEach(mealOption => {
                    const option = document.createElement('option');
                    option.value = mealOption;
                    option.text = mealOption;
                    if (meal === mealOption) {
                        option.selected = true;
                    }
                    mealSelect.appendChild(option);
                });
                col3.appendChild(mealSelect);

                const deleteButton = document.createElement('button');
                deleteButton.type = 'button';
                deleteButton.className = 'btn btn-danger mb-2';
                col4.className = 'col-auto';

                const trashIcon = document.createElement('i');
                trashIcon.className = 'bi bi-trash3 fs-6';

                deleteButton.appendChild(trashIcon);
                col4.appendChild(deleteButton);

                rowContainer.appendChild(col1);
                rowContainer.appendChild(col2);
                rowContainer.appendChild(col3);
                rowContainer.appendChild(col4);

                dayInputsContainer.appendChild(rowContainer);

                deleteButton.addEventListener('click', () => {
                    dayInputsContainer.removeChild(rowContainer);
                });
            }
        }
    }

    document.getElementById('updateCurrentDiet').onclick = function () {
        const updatedPlanName = document.getElementById('planNameId').value;
        const updatedDiet = {};


        for (let day in diet) {
            const dayInputsContainer = document.getElementById(`${day.toLowerCase()}`);
            const rows = dayInputsContainer.getElementsByClassName('row');

            updatedDiet[day] = {};

            for (let row of rows) {
                const foodInput = row.querySelector(`input[name="${day.toLowerCase()}Food"]`);
                const quantityInput = row.querySelector(`input[name="${day.toLowerCase()}Qty"]`);
                const mealSelect = row.querySelector('select');

                if (!updatedDiet[day][mealSelect.value]) {
                    updatedDiet[day][mealSelect.value] = {};
                }

                updatedDiet[day][mealSelect.value][foodInput.value] = quantityInput.value;
            }
        }
        if (idDiet === -1)
            throw new Error('Invalid id')
        const updatedData = {
            idUser: -1,
            id: idDiet,
            dietDate: '',
            planName: updatedPlanName,
            diet: updatedDiet
        };
        fetch('http://localhost:8080/cycleK-1.0.0/rest/diet', {
            credentials: 'include',
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        }).then(response => {
            if (!response.ok) {
                // Check for 401 Unauthorized
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                }
                // Throw an error for other non-success statuses
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        })
            .then(data => {
                window.location.reload();
            })
            .catch(error => {
                if (error.message === 'Unauthorized') {
                    console.error('Error 401: Unauthorized - Redirecting to login page.');
                    // Redirect to login page
                    window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
                } else {
                    console.error('Error:', error);
                    alert("DietPlan cant be modified 24 hours has passed!");
                }
            });
        $('#editDietModal').modal('hide')
    }
}
