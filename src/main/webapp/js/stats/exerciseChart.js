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
    let exercises = [];

    const fetchDiet = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/exercises/user/', {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                // Check for 401 Unauthorized
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                }
                // Throw an error for other non-success statuses
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            dietStats.bestEx = data['ExercisePlanStats']['bestEx'];
            dietStats.bestExWeight = data['ExercisePlanStats']['bestExWeight'];
            dietStats.worstEx = data['ExercisePlanStats']['worstEx'];
            dietStats.worstExWeight = data['ExercisePlanStats']['worstExWeight'];
            dietStats.favEx = data['ExercisePlanStats']['favEx'];
            dietStats.favExCount = data['ExercisePlanStats']['favExCount'];
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    const fetchExercises = fetch('http://localhost:8080/cycleK-1.0.0/rest/exercises')
        .then(response => {
            if (!response.ok) {
                // Check for 401 Unauthorized
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                }
                // Throw an error for other non-success statuses
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            exercises = data['resource-list'];
            })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    Promise.all([fetchDiet, fetchExercises]).then(() => {
        dietStats.bestEx = exercises.find(ex => ex.exercise.id === dietStats.bestEx).exercise.exercise_name
        dietStats.worstEx = exercises.find(ex => ex.exercise.id === dietStats.worstEx).exercise.exercise_name
        dietStats.favEx = exercises.find(ex => ex.exercise.id === dietStats.favEx).exercise.exercise_name
        
        favEx.innerHTML = dietStats.favEx;
        favExCount.innerHTML = dietStats.favExCount;
        bestEx.innerHTML = dietStats.bestEx;
        bestExWeight.innerHTML = dietStats.bestExWeight;
        worstEx.innerHTML = dietStats.worstEx;
        worstExWeight.innerHTML = dietStats.worstExWeight;
    }); 
});