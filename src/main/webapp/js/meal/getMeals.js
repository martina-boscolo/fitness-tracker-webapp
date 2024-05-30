fetch('http://localhost:8080/cycleK-1.0.0/rest/meal/user',{
    credentials:'include'
})
.then(response => response.json())
.then(data => {})
.catch(error => console.error('Error: ', error));