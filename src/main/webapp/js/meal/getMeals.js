document.addEventListener('DOMContentLoaded', function () {
    fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/user', {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Response from db was not ok");
            return response.json();
        })
        .then(data => {
            const mealsExtracted = extractMeals(data);
        })
        .catch(error => console.error('Error: ', error));
});

function extractMeals(json) {
    const meals = [];

    json["resource-list"].forEach(resource => {
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
            mealType: meal.mealType,
            foods: meal.meal.meal
        };

        const foodMap = extractedMeal.foods.reduce((map, food) => {
            map[food.idFood] = food.qty;
            return map;
        }, {});

        const fetchPromises = Object.keys(foodMap).map(key => fetchFood(key).then(foodData => {
            const qty = foodMap[foodData.food.id];

            totFoods.push(foodData.food.fdnm);
            totKcal += (Number(foodData.food.kcal) * qty / 100);
            totCarbs += (Number(foodData.food.carbs) * qty / 100);
            totFats += (Number(foodData.food.fats) * qty / 100);
            totProt += (Number(foodData.food.prot) * qty / 100);
        }));

        Promise.all(fetchPromises)
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
    });
    console.log("meals: ", meals);
    return meals;
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

