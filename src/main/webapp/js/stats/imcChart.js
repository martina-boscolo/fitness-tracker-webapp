document.addEventListener('DOMContentLoaded', () => {
    const imc = {
        global: NaN,
        mean: NaN,
        user: NaN
    };

    let imcValue = document.getElementById('imc-value');
    let imcMean = document.getElementById('imc-mean');
    let imcGlobal = document.getElementById('imc-global');

    const fetchStats = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/imc/mean')
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
                window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
            } else {
                console.error('Error:', error);
            }
        });

    const fetchGoals = fetch('http://localhost:8080/cycleK-1.0.0/rest/stats/imc/user/', {
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
                window.location.href = 'http://localhost:8080/cycleK-1.0.0/html/login.html'; // Adjust the URL as needed
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
        

    })
});

function getImcRange(imc) {
    if (imc < 16.9) return ['underweight', 'text-danger'];
    if (imc < 18.4) return ['slightly underweight', 'text-warning'];
    if (imc < 24.9) return ['normal', 'text-success'];
    if (imc < 29.9) return ['overweight', 'text-warning'];
    if (imc < 34.9) return ['obesity class 1', 'text-danger'];
    if (imc < 39.9) return ['obesity class 2', 'text-danger'];
    return ['obesity class 3', 'text-danger'];
}
