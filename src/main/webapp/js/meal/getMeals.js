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
            console.log("RAW DATA: ", data);
            const mealsExtracted = extractMeals(data);
            console.log(mealsExtracted);
        })
        .catch(error => console.error('Error: ', error));
});

function extractMeals(json) {
    const meals = [];

    json["resource-list"].forEach(resource => {
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
        console.log("mappa: ", foodMap);
        for(const key of foodMap.keys()){
            fetchFood(key);
        }
        meals.push(extractedMeal);
    });

    return meals;
}

function fetchFood(id) {
    fetch(`http://localhost:8080/cycleK-1.0.0/rest/foods/id/${id}`, {
        method: "GET",
        mode: "cors",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Response from db was not ok");
            return response.json();
        })
        .then(data => {
            console.log("FOOD DATA: ", data);
        })
        .catch(error => console.error('Error: ', error));
}