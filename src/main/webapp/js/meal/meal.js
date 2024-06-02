document.addEventListener('DOMContentLoaded', function () {
    fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/user', {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Response from db was not ok");
            return response.json();
        })
        .then(data => extractMeals(data))
        .then(mealsExtracted => {
            //tested, it works
            for (const mealExtracted of mealsExtracted) {
                populateMeal(mealExtracted);
            }
        })
        .catch(error => console.error('Error: ', error));

    if (checkAuth())
        loadFoods();
});


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

window.addFood = function addFood(mealType) {
    const select = document.getElementById(`${mealType}-foods`);
    const gramsInput = document.getElementById(`${mealType}-grams`);
    const foodId = select.value;
    const foodName = select.options[select.selectedIndex].text;
    const grams = gramsInput.value;

    if (foodId && grams) {
        mealsToDisplay[mealType].push({ idFood: parseInt(foodId), qty: parseInt(grams) });

        const foodList = document.getElementById(`${mealType}-list`);
        const listItem = document.createElement('li');
        listItem.textContent = `${foodName} - ${grams} grams`;
        foodList.appendChild(listItem);

        gramsInput.value = '';
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
        id: null,
        idUser: null,
        date: null,
        mealType: mealTypeNumber,
        meal: JSON.stringify({
            meal: mealsToDisplay[mealType]
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

const mealsToDisplay = {
    breakfast: [],
    lunch: [],
    snack: [],
    dinner: []
};

function extractMeals(json) {
    const meals = [];
    const mealTypes = ['breakfast', 'lunch', 'snack', 'dinner'];

    const fetchPromises = json["resource-list"].map(resource => {
        let totFoods = [];
        let totKcal = 0;
        let totCarbs = 0;
        let totFats = 0;
        let totProt = 0;

        const meal = resource.meal;
        const extractedMeal = {
            id: meal.id,
            idUser: meal.idUser,
            date: meal.date,
            mealType: mealTypes[meal.mealType - 1],
            foods: meal.meal.meal
        };

        const foodMap = extractedMeal.foods.reduce((map, food) => {
            map[food.idFood] = food.qty;
            return map;
        }, {});

        const todayDate = getCurrentDateFormatted();
        if (extractedMeal.date === todayDate) {
            const foodFetchPromises = Object.keys(foodMap).map(key => fetchFood(key).then(foodData => {
                const qty = foodMap[foodData.food.id];

                totFoods.push(foodData.food.fdnm);
                totKcal += (Number(foodData.food.kcal) * qty / 100);
                totCarbs += (Number(foodData.food.carbs) * qty / 100);
                totFats += (Number(foodData.food.fats) * qty / 100);
                totProt += (Number(foodData.food.prot) * qty / 100);
            }));

            return Promise.all(foodFetchPromises)
                .then(() => {
                    extractedMeal.totFoods = totFoods;
                    extractedMeal.totKcal = totKcal;
                    extractedMeal.totCarbs = totCarbs;
                    extractedMeal.totFats = totFats;
                    extractedMeal.totProt = totProt;
                    meals.push(extractedMeal);
                })
                .catch(error => {
                    console.error('Error fetching foods: ', error);
                });
        } else {
            return Promise.resolve();
        }
    });

    return Promise.all(fetchPromises).then(() => meals);
}


function fetchFood(id) {
    return fetch(`http://localhost:8080/cycleK-1.0.0/rest/foods/id/${id}`, {
        method: "GET",
        mode: "cors",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Response from db was not ok");
            }
            return response.json();
        })
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('Error: ', error);
            throw error;
        });
}

function getCurrentDateFormatted() {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

function populateMeal(mealData) {
    const mealFoods = document.getElementById(`${mealData.mealType}_foods`);
    const mealKcal = document.getElementById(`${mealData.mealType}_kcal`);
    const mealCarbs = document.getElementById(`${mealData.mealType}_carbs`);
    const mealProt = document.getElementById(`${mealData.mealType}_prot`);
    const mealFats = document.getElementById(`${mealData.mealType}_fats`);

    mealFoods.textContent = 'Foods: ' + mealData.totFoods.join(', ');
    mealKcal.textContent = `Calories: ${mealData.totKcal}`;
    mealCarbs.textContent = `Carbs: ${mealData.totCarbs}g`;
    mealProt.textContent = `Proteins: ${mealData.totProt}g`;
    mealFats.textContent = `Fats: ${mealData.totFats}g`;
}