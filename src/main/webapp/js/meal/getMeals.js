document.addEventListener('DOMContentLoaded', function () {
    fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/user', {
        credentials: 'include'
    })
        .then(response => {
            if(!response.ok)
                throw new Error("Response from db was not ok");
            console.log(response.json());
            return response.json()
        })
        .then(data => {
            console.log(data);

        })
        .catch(error => console.error('Error: ', error));
});