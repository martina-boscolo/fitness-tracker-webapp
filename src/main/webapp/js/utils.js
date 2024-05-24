const BASE_URL = 'http://localhost:8080/cycleK-1.0.0/';
const HOMEPAGE_URL = 'http://localhost:8080/cycleK-1.0.0/jsp/stats.jsp';
const COOKIE_DURATION = 120;

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

function logIn(token) {
    setCookie('authToken', token, COOKIE_DURATION); // Set the token as a cookie
    window.location.href = HOMEPAGE_URL;
}

function setCookie(name, value, minutes, path = '/', domain = window.location.hostname, secure = true) {
    let cookie = `${name}=${encodeURIComponent(value)};`;

    if (minutes) {
        const date = new Date();
        date.setTime(date.getTime() + (minutes * 60 * 1000));
        cookie += `expires=${date.toUTCString()};`;
    }

    cookie += `path=${path};`;

    if (domain) {
        cookie += `domain=${domain};`;
    }

    if (secure) {
        cookie += `secure;`;
    }

    document.cookie = cookie;
}