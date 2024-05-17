document.addEventListener('DOMContentLoaded', () => {
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

    let height = document.getElementById('height');

    const fetchStats = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/body/user/1')
        .then(response => response.json())
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
        .catch(error => console.error('Error:', error));

    const fetchGoals = fetch('http://localhost:8080/cycleK-1.0.0/rest/goals/user/1')
        .then(response => response.json())
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
        .catch(error => console.error('Error:', error));

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
    })
});

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
                borderWidth: 1,
                fill: true
            },
            {
                label: 'Goal',
                data: goals,
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderWidth: 1,
                fill: true
            }
            ]
        },
        options: {
            scales: {
                y: {
                    min: min,
                    max: max,
                    title: {
                        display: true,
                        text: 'Weight (Kg)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Date'
                    }
                }
            }
        }
    });

    document.getElementById('bodyChartType').addEventListener('change', function(event) {
        const selectedType = document.querySelector('#bodyChartType input[name="bodyType"]:checked').value;
        bodychart.config.type = selectedType;
        bodychart.update();
    });

    document.getElementById('bodyChartFill').addEventListener('change', function(event) {
        const selectedType = document.querySelector('#bodyChartFill input[name="bodyFill"]:checked').value;
        bodychart.data.datasets.forEach(dataset => {
            dataset.fill = selectedType === 'true';
        });
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

