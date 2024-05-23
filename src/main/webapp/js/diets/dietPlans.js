fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/idUser/' , {
    credentials: 'include'
})
    .then(response => response.json())
    .then(data => {
        console.log(data);

        let currentPlanIndex = 0; // Keep track of the current displayed diet plan

        // Function to display a specific diet plan
        function displayDietPlan(planIndex) {
            const dietPlan = data['resource-list'][planIndex];
            let planName = dietPlan['diet']['planName'];
            let diet = dietPlan['diet']['diet'];

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

                tabLink.style.color = 'orange';
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
            prevButton.setAttribute("class","bi bi-arrow-left-circle-fill")
        });
        prevButton.addEventListener('mouseout', () => {
            prevButton.setAttribute("class", "bi bi-arrow-left-circle")
        });

        const nextButton = document.getElementById('nextDiet');
        nextButton.addEventListener('click', nextDietPlan);
        nextButton.addEventListener('mouseover', () => {
            nextButton.setAttribute("class","bi bi-arrow-right-circle-fill")
        });
        nextButton.addEventListener('mouseout', () => {
            nextButton.setAttribute("class","bi bi-arrow-right-circle")
        });

        $('#editDietModal').on('shown.bs.modal', function (e) {
            updateDiet(data['resource-list'][currentPlanIndex]);
        });

    })
    .catch(error => console.error('Error:', error));

function updateDiet(data) {
    // Fetch the selected diet plan
    console.log(data)
    const diet = data['diet']['diet'];
    console.log(diet)
    const planName = data['diet']['planName'];
    console.log(planName)
        const form = document.getElementById('dietForm');

        // Clear existing form content
        form.innerHTML = '';

        // Create input field for the plan name
        const planNameFieldset = document.createElement('fieldset');
        const planNameLabel = document.createElement('label');
        planNameLabel.textContent = 'Plan Name:';
        const planNameInput = document.createElement('input');
        planNameInput.type = 'text';
        planNameInput.name = 'planName';
        planNameInput.value = planName; // Set planName value here
        planNameFieldset.appendChild(planNameLabel);
        planNameFieldset.appendChild(planNameInput);
        form.appendChild(planNameFieldset);

        // Iterate through diet plan data and create form fields for meals and foods
        for (let day in diet) {
            // Only process keys that are not 'planName' or 'dietDate'
            if (day !== 'planName' && day !== 'dietDate') {
                for (let meal in diet[day]) {
                    // Create fieldset for each meal
                    const fieldset = document.createElement('fieldset');
                    const legend = document.createElement('legend');
                    legend.textContent = `${day} - ${meal}`;
                    fieldset.appendChild(legend);

                    // Iterate through foods in the meal
                    for (let food in diet[day][meal]) {
                        const foodData = diet[day][meal][food];

                        // Create input elements for each food name and quantity
                        if (typeof foodData === 'object') {
                            // If food is an object (contains subfoods)
                            for (let subFood in foodData) {
                                // Create input field for subFood name
                                const nameLabel = document.createElement('label');
                                nameLabel.textContent = `${subFood}:`;
                                const nameInput = document.createElement('input');
                                nameInput.type = 'text';
                                nameInput.name = `${day}-${meal}-${food}-${subFood}`;
                                nameInput.value = subFood;
                                fieldset.appendChild(nameLabel);
                                fieldset.appendChild(nameInput);
                                fieldset.appendChild(document.createTextNode(': '));

                                // Create input field for quantity
                                const quantityInput = document.createElement('input');
                                quantityInput.type = 'text';
                                quantityInput.name = `${day}-${meal}-${food}-${subFood}-quantity`;
                                quantityInput.value = foodData[subFood];
                                fieldset.appendChild(quantityInput);
                                fieldset.appendChild(document.createElement('br'));
                            }
                        } else {
                            // If food is a direct value
                            // Create input field for food name
                            const nameLabel = document.createElement('label');
                            nameLabel.textContent = `${food}:`;
                            const nameInput = document.createElement('input');
                            nameInput.type = 'text';
                            nameInput.name = `${day}-${meal}-${food}`;
                            nameInput.value = food;
                            fieldset.appendChild(nameLabel);
                            fieldset.appendChild(nameInput);
                            fieldset.appendChild(document.createTextNode(': '));

                            // Create input field for quantity
                            const quantityInput = document.createElement('input');
                            quantityInput.type = 'text';
                            quantityInput.name = `${day}-${meal}-${food}-quantity`;
                            quantityInput.value = foodData;
                            fieldset.appendChild(quantityInput);
                            fieldset.appendChild(document.createElement('br'));
                        }
                    }

                    // Append fieldset to the form
                    form.appendChild(fieldset);
                }
            }
        }
    }



