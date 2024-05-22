fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/idUser/1')
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

    })
    .catch(error => console.error('Error:', error));
