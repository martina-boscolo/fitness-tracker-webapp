document.addEventListener('DOMContentLoaded', function () {
    // Fetch and display existing diet plans
    fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/')
        .then(response => response.json())
        .then(data => {

            // Function to display a specific diet plan
            function displayDietPlan(planIndex) {
                const dietPlan = data['resource-list'][planIndex];
                let planName = dietPlan['diet']['planName'];
                let dietDate = dietPlan['diet']['dietDate'];
                let diet = dietPlan['diet']['diet'];

                // Clear existing content except for navigation buttons
                const contentContainer = document.getElementById('content');
                contentContainer.innerHTML = '';

                // Display the plan name and diet date
                const dataContainer = document.createElement('div');
                dataContainer.innerHTML = `<h1 class="mb-4">Diet Plan: ${planName}</h1>`;

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
                    tabLink.setAttribute('aria-selected', isFirstDay.toString());

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

            // Handle form submission
            document.getElementById('addDietForm').addEventListener('submit', function(event) {
                event.preventDefault();

                const planName = document.getElementById('planName').value;

                // Construct diet object based on form input
                const newDiet = {
                    idUser: 1,
                    planName: planName,
                    diet: {}
                };

                // Iterate through each day of the week
                const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
                daysOfWeek.forEach(day => {
                    const dayMeals = {};
                    const dayInputs = document.querySelectorAll(`input[placeholder^="${day}"]`);
                    dayInputs.forEach(input => {
                        const mealType = input.placeholder.split(' ')[1].toLowerCase(); // Extract meal type (breakfast, lunch, dinner)
                        if (!dayMeals[mealType]) {
                            dayMeals[mealType] = {};
                        }
                        dayMeals[mealType][input.name] = input.value;
                    });
                    newDiet.diet[day] = dayMeals;
                });


                function addFoodInput(containerId) {
                    const container = document.getElementById(containerId);
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
                // Send the new diet plan to the backend
                fetch('http://localhost:8080/cycleK-1.0.0/rest/diet', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(newDiet)
                })
                    .then(response => response.json())
                    .then(addedDiet => {
                        console.log('Diet plan added:', addedDiet);

                        // Update the local data and display the new diet plan
                        data['resource-list'].push({ diet: addedDiet });
                        displayDietPlan(data['resource-list'].length - 1); // Show the newly added diet plan
                    })
                    .catch(error => console.error('Error:', error));
            });
        })
        .catch(error => console.error('Error:', error));
});
