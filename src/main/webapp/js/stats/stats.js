document.addEventListener('DOMContentLoaded', function() {

    // Check if authToken cookie is present
    let Cookies = document.cookie;
    if (checkAuth()) {
        fetchBodyStats();
        fetchImc();
        fetchDiet();
        fetchExercise();
        formListener();
    }

    /* Popover */
    $(document).ready(function(){
        $('[data-bs-toggle="popover"]').popover({
            trigger: 'focus'
        });
    });
});

// new Stats
function formListener() {
    const submitButton = document.getElementById('submitStats');
    const closeButton = document.getElementById('closeStats');
    const form = document.getElementById('statsForm');

    submitButton.addEventListener('click', (event) => {
        if (form.checkValidity()) {
            const formData = new FormData(form);

            const bodyStats = {
                height: formData.get('height'),
                weight: formData.get('weight'),
                fatty: formData.get('fatty'),
                lean: formData.get('lean')
            };

            console.log(JSON.stringify(bodyStats));

            const rest = REST_URL + formData.get('stats_goal');
            const fetchStats = fetch(rest, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bodyStats)
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
                .then(
                    data => {
                        console.log(data);
                        $('#statsModal').modal('hide');
                        location.reload();
                    }
                )
                .catch(error => {
                    if (error.message === 'Unauthorized') {
                        console.error('Error 401: Unauthorized - Redirecting to login page.');
                        // Redirect to login page
                        window.location.href = LOGIN; // Adjust the URL as needed
                    } else {
                        console.error('Error:', error);
                    }
                });

        } else {
            form.reportValidity();
        }
    });

    closeButton.addEventListener('click', (event) => {
        let tmp = form.querySelector('input[name="height"]');
        form.reset();
        form.querySelector('input[name="height"]').value = tmp.value;
    });
}

// Body Stats
function fetchBodyStats() {
    const stats = {
        dates: [],
        weights: [],
        height: [],
        fatty: [],
        lean: []
    };

    const goals = {
        dates: [],
        weights: [],
        height: [],
        fatty: [],
        lean: []
    };

    let height = document.getElementById('form-height');

    const fetchStats = fetch(REST_URL + 'stats/body/user/', {
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
            data['resource-list'].forEach(item => {
                const bodyStats = item['bodyStats'];
                stats.dates.push(bodyStats['statsDate']);
                stats.weights.push(bodyStats['weight']);
                stats.height.push(bodyStats['height']);
                stats.fatty.push(bodyStats['fatty']);
                stats.lean.push(bodyStats['lean']);
            });
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    const fetchGoals = fetch(REST_URL + 'goals/user/', {
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
            data['resource-list'].forEach(item => {
                const userGoals = item['userGoals'];
                goals.dates.push(userGoals['goalDate']);
                goals.weights.push(userGoals['weight']);
                goals.height.push(userGoals['height']);
                goals.fatty.push(userGoals['fatty']);
                goals.lean.push(userGoals['lean']);
            });
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    Promise.all([fetchStats, fetchGoals]).then(() => {
        const labels = Array.from(new Set([...stats.dates, ...goals.dates])).sort((a, b) => new Date(a) - new Date(b));

        const getValues = (dates, values) => labels.map(label => dates.includes(label) ? values[dates.indexOf(label)] : null);

        const w_stats = getValues(stats.dates, stats.weights);
        const w_goals = getValues(goals.dates, goals.weights);
        const f_stats = getValues(stats.dates, stats.fatty);
        const f_goals = getValues(goals.dates, goals.fatty);
        const l_stats = getValues(stats.dates, stats.lean);
        const l_goals = getValues(goals.dates, goals.lean);

        height.value = stats.height[0];
        weightChart(labels, w_stats, w_goals, f_stats, f_goals, l_stats, l_goals)
    });
}

function weightChart(labels, w_stats, w_goals, f_stats, f_goals, l_stats, l_goals) {

    let stats = w_stats
    let goals = w_goals

    // compute min among stats and goals value excluding null
    let min = Math.min(...stats.filter(x => x != null), ...goals.filter(x => x != null)) - 2
    let max = Math.max(...stats.filter(x => x != null), ...goals.filter(x => x != null)) + 2

    const ctx = document.getElementById('bodyChart').getContext('2d');
    var bodychart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Weight',
                data: stats,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                pointRadius: 6,
                borderWidth: 1
            },
            {
                label: 'Goal',
                data: goals,
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                pointRadius: 6,
                borderWidth: 1
            }
            ]
        },
        options: {
            spanGaps: true,
            scales: {
                y: {
                    min: min,
                    max: max,
                    title: {
                        display: true,
                        text: 'Weight (Kg)',
                        color: '#f6f6f6',
                        font: {size:16}
                    },
                    ticks: {
                        font: {size:14},
                        color: '#f6f6f6' // Colore delle etichette dell'asse x
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.2)', // Colore della griglia dell'asse x
                        borderColor: '#f6f6f6' // Colore del bordo dell'asse x
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Date',
                        color: '#f6f6f6',
                        font: {size:16}
                    },
                    ticks: {
                        font: {size:14},
                        color: '#f6f6f6' // Colore delle etichette dell'asse x
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.2)', // Colore della griglia dell'asse x
                        borderColor: '#f6f6f6' // Colore del bordo dell'asse x
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: { size:18 },
                        color: '#f6f6f6' // Colore delle etichette della legenda
                    }
                }
            }
        }
    });

    document.getElementById('bodyChartType').addEventListener('change', function(event) {
        bodychart.config.type = document.querySelector('#bodyChartType input[name="bodyType"]:checked').value;
        bodychart.update();
    });

    document.getElementById('bodyChartMetric').addEventListener('change', function(event) {
        const selectedMetric = document.querySelector('#bodyChartMetric input[name="bodyMetric"]:checked').getAttribute('id');
            var newstats = [];
            var newgoals = [];
            var newLabel = '';
            var y_text = '';
            switch (selectedMetric) {
                case 'weight':
                    newstats = stats;
                    newgoals = goals;
                    newLabel = 'Weight';
                    y_text = 'Weight (Kg)';
                    break;
                case 'fatty':
                    newstats = f_stats;
                    newgoals = f_goals;
                    newLabel = 'Fat';
                    y_text = 'Perc (%)';
                    break;
                case 'lean':
                    newstats = l_stats;
                    newgoals = l_goals;
                    newLabel = 'Lean';
                    y_text = 'Perc (%)';
                    break;
                default:
                    return;
            }

            bodychart.data.datasets[0].data = newstats;
            bodychart.data.datasets[0].label = newLabel;
            bodychart.data.datasets[1].data = newgoals;
            bodychart.data.datasets[1].label = newLabel + ' Goal';
            let min = Math.min(...bodychart.data.datasets[0].data.filter(x => x != null), ...bodychart.data.datasets[1].data.filter(x => x != null)) - 2
            let max = Math.max(...bodychart.data.datasets[0].data.filter(x => x != null), ...bodychart.data.datasets[1].data.filter(x => x != null)) + 2
            bodychart.options.scales.y.min = min;
            bodychart.options.scales.y.max = max;
            bodychart.options.scales.y.title.text = y_text;
            bodychart.update();
        });
        
    
}

// IMC
function fetchImc() {
    const imc = {
        global: NaN,
        mean: NaN,
        user: NaN
    };

    let imcValue = document.getElementById('imc-value');
    let imcMean = document.getElementById('imc-mean');
    let imcGlobal = document.getElementById('imc-global');

    const fetchStats = fetch(REST_URL + 'stats/imc/mean')
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
            imc.global = data['GlobalMeanImc'];
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    const fetchGoals = fetch(REST_URL + 'stats/imc/user/', {
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
            imc.mean = data['userImc']['userMeanImc'];
            imc.user = data['userImc']['imc'];
        })
        .catch(error => {
            if (error.message === 'Unauthorized') {
                console.error('Error 401: Unauthorized - Redirecting to login page.');
                // Redirect to login page
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    Promise.all([fetchStats, fetchGoals]).then(() => {
        
        // lambda function to round
        const round = (value) => Number(Math.round((value + Number.EPSILON) * 10) / 10);

        let u_imc = round(imc.user);
        let [descr, color] = getImcRange(u_imc);
        imcValue.innerHTML = u_imc + ' (' + descr + ')';
        imcValue.className = 'imc-value ' + color;
            

        imcMean.innerHTML = round(imc.user - imc.mean);
        imcGlobal.innerHTML = round(imc.user - imc.global);
        

    });
}

function getImcRange(imc) {
    if (imc < 16.9) return ['underweight', 'text-danger'];
    if (imc < 18.4) return ['slightly underweight', 'text-warning'];
    if (imc < 24.9) return ['normal', 'text-success'];
    if (imc < 29.9) return ['overweight', 'text-warning'];
    if (imc < 34.9) return ['obesity class 1', 'text-danger'];
    if (imc < 39.9) return ['obesity class 2', 'text-danger'];
    return ['obesity class 3', 'text-danger'];
}

// Diet
function fetchDiet() {
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

    const fetchDiet = fetch(REST_URL + 'stats/meals/user/', {
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
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    Promise.all([fetchDiet]).then(() => {
        favFood.innerHTML = dietStats.fav;
        favCount.innerHTML = dietStats.coun;
        dietChart(dietStats)
    });
}

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
            spanGaps: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        font: {size:14},
                        color: '#f6f6f6' // Colore delle etichette dell'asse x
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.2)', // Colore della griglia dell'asse x
                        borderColor: '#f6f6f6' // Colore del bordo dell'asse x
                    }
                },
                x: {
                    ticks: {
                        font: {size:14},
                        color: '#f6f6f6' // Colore delle etichette dell'asse x
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.2)', // Colore della griglia dell'asse x
                        borderColor: '#f6f6f6' // Colore del bordo dell'asse x
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: { size:18 },
                        color: '#f6f6f6' // Colore delle etichette della legenda
                    }
                }
            }
        }
    });

    document.getElementById('dietChartType').addEventListener('change', function(event) {
        dietChart.config.type = document.querySelector('#dietChartType input[name="dietType"]:checked').value;
        dietChart.update();
    });

}

// Exercise
function fetchExercise() {
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

    const fetchDiet = fetch(REST_URL + 'stats/exercises/user/', {
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
                window.location.href = LOGIN; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    const fetchExercises = fetch(REST_URL + 'exercises')
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
                window.location.href = LOGIN; // Adjust the URL as needed
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
}
