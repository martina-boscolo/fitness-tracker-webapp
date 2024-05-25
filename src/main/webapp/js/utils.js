const BASE_URL = 'http://localhost:8080/cycleK-1.0.0/';
const REST_URL = BASE_URL + 'rest/';

const LOGIN = BASE_URL;
const SIGNIN = BASE_URL + 'html/register.html';
const PROFILE = BASE_URL + 'jsp/profile.jsp';
const EXERCISE = BASE_URL + 'jsp/exercise.jsp';
const DIET = BASE_URL + 'jsp/diet.jsp';
const MEAL = BASE_URL + 'jsp/meal.jsp';
const STATS = BASE_URL + 'jsp/stats.jsp';
const SOCIAL = BASE_URL + 'jsp/social.jsp';

const HOMEPAGE_URL = STATS;
const COOKIE_DURATION = 120;

function checkAuth() {
    let Cookies = document.cookie;
    if (Cookies.indexOf('authToken') === -1) {
        console.error('Error: Unauthorized - Redirecting to login page.');
        // Redirect to login page
        window.location.href = LOGIN; // Adjust the URL as needed
        return false;
    }
    else
        return true;
}

function logOut() {
    document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = LOGIN;
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