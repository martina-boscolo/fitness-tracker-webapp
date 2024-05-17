
fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/idUser/1')
    .then(response => response.json())
    .then(data => {
        console.log(data);

        // Assuming only one diet plan is needed
        let dietPlan = data['resource-list'][0];
        let planName = dietPlan['diet']['planName'];
        let dietDate = dietPlan['diet']['dietDate'];
        let diet = dietPlan['diet']['diet'];

        // Display the plan name and diet date
        const dataContainer = document.getElementById('dataContainer');
        dataContainer.innerHTML = `<h1 class="mb-4">Diet Plan: ${planName}</h1>`;

        // Iterate through the days in the diet
        for (let day in diet) {
            const dayElement = document.createElement('div');
            dayElement.classList.add('day', 'mb-4'); // Add both "day" and "mb-4" for spacing
            dayElement.innerHTML = `<h2 class="text-primary">${day}</h2>`;

            // Iterate through the meals of the day
            for (let meal in diet[day]) {
                const mealElement = document.createElement('div');
                mealElement.classList.add('meal', 'mb-3');
                mealElement.innerHTML = `<h3 class="text-secondary">${meal}</h3>`;

                // Create a list group for the food items
                const foodList = document.createElement('ul');
                foodList.classList.add('list-group');

                // Iterate through the foods in the meal
                for (let food in diet[day][meal]) {
                    const foodItemElement = document.createElement('li');
                    foodItemElement.classList.add('list-group-item');

                    if (typeof diet[day][meal][food] === 'object') {
                        // If the food item is an object, iterate through its properties
                        foodItemElement.textContent = `${food}:`;
                        const nestedList = document.createElement('ul');
                        nestedList.classList.add('list-group', 'list-group-flush');
                        for (let subFood in diet[day][meal][food]) {
                            const subFoodItemElement = document.createElement('li');
                            subFoodItemElement.classList.add('list-group-item');
                            subFoodItemElement.textContent = `${subFood}: ${diet[day][meal][food][subFood]} grams`;
                            nestedList.appendChild(subFoodItemElement);
                        }
                        foodItemElement.appendChild(nestedList);
                    } else {
                        // If the food item is not an object, display it directly
                        foodItemElement.textContent = `${food}: ${diet[day][meal][food]} grams`;
                    }

                    foodList.appendChild(foodItemElement);
                }

                mealElement.appendChild(foodList);
                dayElement.appendChild(mealElement);
            }

            dataContainer.appendChild(dayElement);
        }
    })
    .catch(error => console.error('Error:', error));




/*const fetchDiet = fetch('http://localhost:8080/cycleK-1.0.0/rest/diet/idUser/1')
    .then(response => response.json())
    .then(data => {
        console.log(data);
        let name = data['resource-list'][0]['diet']['planName'];
        let date = data['resource-list'][0]['diet']['dietDate'];
        let diet = data['resource-list'][0]['diet']['diet'];
        console.log(name);
        console.log(date);
        for (let day in diet) {
            console.log(day);
            for (let meal in diet[day]) {
                console.log(meal);
                for (let food in diet[day][meal]) {
                    console.log(food);
                    console.log(diet[day][meal][food]);
                }
            }
        }
    })
    .catch(error => console.error('Error:', error));


Promise.all([fetchDiet]).then(() => {

});
*/