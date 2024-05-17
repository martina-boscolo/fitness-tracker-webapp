document.addEventListener('DOMContentLoaded', () => {
    const dietStats = {
        bestEx: NaN,
        bestExWeight: NaN,
        worstEx: NaN,
        worstExWeight: NaN,
        favEx: NaN,
        favExCount: NaN
    };

    let favEx = document.getElementById('fav-ex');
    let favExCount = document.getElementById('fav-ex-count');
    let bestEx = document.getElementById('best-ex');
    let bestExWeight = document.getElementById('best-ex-weight');
    let worstEx = document.getElementById('worst-ex');
    let worstExWeight = document.getElementById('worst-ex-weight');

    const fetchDiet = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/exercises/user/1')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            dietStats.bestEx = data['ExercisePlanStats']['bestEx'];
            dietStats.bestExWeight = data['ExercisePlanStats']['bestExWeight'];
            dietStats.worstEx = data['ExercisePlanStats']['worstEx'];
            dietStats.worstExWeight = data['ExercisePlanStats']['worstExWeight'];
            dietStats.favEx = data['ExercisePlanStats']['favEx'];
            dietStats.favExCount = data['ExercisePlanStats']['favExCount'];
        })
        .catch(error => console.error('Error:', error));

    const fetchExercises = fetch('http://localhost:8080/cycleK-1.0.0/rest/exercises')
        .then(response => response.json())
        .then(data => {
            dietStats.bestEx = data['resource-list'].find(ex => ex.exercise.id === dietStats.bestEx).exercise.exercise_name
            dietStats.worstEx = data['resource-list'].find(ex => ex.exercise.id === dietStats.worstEx).exercise.exercise_name
            dietStats.favEx = data['resource-list'].find(ex => ex.exercise.id === dietStats.favEx).exercise.exercise_name
        })
        .catch(error => console.error('Error:', error));

    Promise.all([fetchDiet, fetchExercises]).then(() => {
        favEx.innerHTML = dietStats.favEx;
        favExCount.innerHTML = dietStats.favExCount;
        bestEx.innerHTML = dietStats.bestEx;
        bestExWeight.innerHTML = dietStats.bestExWeight;
        worstEx.innerHTML = dietStats.worstEx;
        worstExWeight.innerHTML = dietStats.worstExWeight;
    }); 
});