document.addEventListener('DOMContentLoaded', () => {
    const dietStats = {
        carb: NaN,
        fats: NaN,
        kcal: NaN,
        prot: NaN,
        fav: '',
        coun: NaN
    };

    let favFood = document.getElementById('fav-food');
    let favCount = document.getElementById('fav-count');

    const fetchDiet = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/meals/user/', {
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
            dietStats.carb = data['MealStats']['avg_carb'];
            dietStats.fats = data['MealStats']['avg_fats'];
            dietStats.kcal = data['MealStats']['avg_kcal'];
            dietStats.prot = data['MealStats']['avg_prot'];
            dietStats.fav = data['MealStats']['fav_food'];
            dietStats.coun = data['MealStats']['fav_food_count'];
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

    Promise.all([fetchDiet]).then(() => {
        favFood.innerHTML = dietStats.fav;
        favCount.innerHTML = dietStats.coun;
        dietChart(dietStats)
    });
});

function dietChart(dietStats) {

    const ctx = document.getElementById('dietChart').getContext('2d');
    const dietChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Carbs', 'Fats', 'Kcal', 'Protein'],
            datasets: [{
                label: 'Average',
                data: [dietStats.carb, dietStats.fats, dietStats.kcal, dietStats.prot],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    document.getElementById('dietChartType').addEventListener('change', function(event) {
        const selectedType = document.querySelector('#dietChartType input[name="dietType"]:checked').value;
        dietChart.config.type = selectedType;
        dietChart.update();
    });

}