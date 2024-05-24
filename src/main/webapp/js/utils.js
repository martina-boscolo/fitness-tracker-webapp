const BASE_URL = 'http://localhost:8080/cycleK-1.0.0/';

function checkAuth() {
    let Cookies = document.cookie;
    if (Cookies.indexOf('authToken') === -1) {
        console.error('Error: Unauthorized - Redirecting to login page.');
        // Redirect to login page
        window.location.href = BASE_URL; // Adjust the URL as needed
        return false;
    }
    else
        return true;
}

function logOut() {
    document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = BASE_URL;
}