document.addEventListener('DOMContentLoaded', (event) => {
    const submitButton = document.getElementById('submitStats');
    const closeButton = document.getElementById('closeStats');
    const form = document.getElementById('statsForm');

    submitButton.addEventListener('click', (event) => {
        if (form.checkValidity()) {
            const formData = new FormData(form);

            const bodyStats = {
                idUser: 1,
                height: formData.get('height'),
                weight: formData.get('weight'),
                fatty: formData.get('fatty'),
                lean: formData.get('lean')
            };

            console.log(JSON.stringify(bodyStats));

            const rest = 'http://localhost:8080/cycleK-1.0.0/rest/' + formData.get('stats_goal');
            const fetchStats = fetch(rest, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bodyStats)
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error('Error:', error));

        } else {
            form.reportValidity();
        }
    });

    closeButton.addEventListener('click', (event) => {
        let tmp = form.querySelector('input[name="height"]');
        form.reset();
        form.querySelector('input[name="height"]').value = tmp.value;
    });

});
