document.addEventListener('DOMContentLoaded', function () {
    loadFoods();

    function loadFoods() {
        fetch('http://localhost:8080/cycleK-1.0.0/rest/foods')
            .then(response => response.json())
            .then(data => {
                const foods = data['resource-list'].map(item => item.food);
                populateDropdowns(foods);
            })
            .catch(error => {
                console.error('Error loading foods:', error);
            });
    }

    function populateDropdowns(foods) {
        const mealTypes = ['breakfast', 'lunch', 'snack', 'dinner'];

        mealTypes.forEach(function (mealType) {
            const select = document.getElementById(`${mealType}-foods`);
            foods.forEach(function (food) {
                const option = document.createElement('option');
                option.value = food.id;
                option.textContent = food.fdnm;
                select.appendChild(option);
            });
        });
    }

    fetchMeals();

    function fetchMeals() {
        console.log("inside fetchMeals");
        fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/user', {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(meals => {
                console.log("Meals inside fetching methods ", meals, " // ", meals.meals);
                displayMeals(meals.meals);
            })
            .catch(error => {
                console.error('Error fetching meals:', error);
            });
    }

    function fetchFoodDetails(idFood) {
        console.log("food details");
        return fetch(`http://localhost:8080/cycleK-1.0.0/rest/foods/id/${idFood}`, {
            credentials: 'include',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(food => {
                console.log(food);
                return food;
            })
            .catch(error => {
                console.error('Error fetching food details:', error);
                return null;
            });
    }

    function calculateNutritionalValues(foods) {
        console.log("computing nutritional values");
        return foods.reduce((totals, food) => {
            totals.calories += food.calories * food.qty / 100;
            totals.protein += food.nutrients.protein * food.qty / 100;
            totals.carbs += food.nutrients.carbs * food.qty / 100;
            totals.fat += food.nutrients.fat * food.qty / 100;
            return totals;
        }, { calories: 0, protein: 0, carbs: 0, fat: 0 });
    }

    function displayMeals(meals) {
        console.log("meals:", meals);
        const mealContainer = document.getElementById('right-container');
        mealContainer.innerHTML = '';
        console.log(mealContainer);

        for (let i = 0; i < meals.length; i++) {
            const meal = meals[i];
            const foodPromises = meal.meal.map(food => fetchFoodDetails(food.idFood));

            Promise.all(foodPromises).then(foodDetails => {
                const enrichedFoods = foodDetails.map((foodDetail, index) => ({
                    ...foodDetail,
                    qty: meal.meal[index].qty
                }));
                const nutritionalValues = calculateNutritionalValues(enrichedFoods);

                const mealDiv = document.createElement('div');
                mealDiv.className = 'meal';
                mealDiv.innerHTML = `
                <h3>Meal Type: ${meal.mealType}</h3>
                <p>Date: ${meal.date}</p>
                <h4>Foods:</h4>
                <ul>
                    ${enrichedFoods.map(food => `<li>${food.name} - ${food.qty}g</li>`).join('')}
                </ul>
                <h4>Nutritional Values:</h4>
                <p>Calories: ${nutritionalValues.calories.toFixed(2)} kcal</p>
                <p>Protein: ${nutritionalValues.protein.toFixed(2)} g</p>
                <p>Carbs: ${nutritionalValues.carbs.toFixed(2)} g</p>
                <p>Fat: ${nutritionalValues.fat.toFixed(2)} g</p>
            `;

                mealContainer.appendChild(mealDiv);
            });
        }
    }


    window.addFood = function addFood(mealType) {
        const select = document.getElementById(`${mealType}-foods`);
        const gramsInput = document.getElementById(`${mealType}-grams`);
        const foodId = select.value;
        const foodName = select.options[select.selectedIndex].text;
        const grams = gramsInput.value;

        if (foodId && grams) {
            meals[mealType].push({ idFood: parseInt(foodId), qty: parseInt(grams) });

            const foodList = document.getElementById(`${mealType}-list`);
            const listItem = document.createElement('li');
            listItem.textContent = `${foodName} - ${grams} grams`;
            foodList.appendChild(listItem);

            gramsInput.value = ''; // Clear the grams input after adding
        } else {
            alert('Please select a food and enter grams.');
        }
    };

    window.registerMeal = function registerMeal(mealType) {
        const mealTypeMap = {
            breakfast: 1,
            lunch: 2,
            snack: 3,
            dinner: 4
        };
        const mealTypeNumber = mealTypeMap[mealType];

        const mealData = {
            id: null, // Left null to be generated by the database
            idUser: null, // Left null to be managed in the backend
            date: null, // Left null to be managed in the backend
            mealType: mealTypeNumber,
            meal: JSON.stringify({
                meal: meals[mealType]
            })
        };

        fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/', {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(mealData)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                alert('Meal registered successfully!');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error registering meal.');
            });
    };
});

const meals = {
    breakfast: [],
    lunch: [],
    snack: [],
    dinner: []
};
