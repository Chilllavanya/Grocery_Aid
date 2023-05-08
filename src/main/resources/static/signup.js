const form = document.getElementById('signup');
const email = document.getElementById('email');
const username = document.getElementById('username');
const password = document.getElementById('password');

const headers = {
    'Content-Type': 'application/json'
};

const handleSubmit = async(e) => {
    e.preventDefault();

    let bodyObj = {
        email: email.value,
        username: username.value,
        password: password.value
    };

    const response = await fetch("http://localhost:8080/api/v1/users/register", {
        method: "POST",
        body: JSON.stringify(bodyObj),
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        window.location.replace(await response.valueOf().text());
    };
}

form.addEventListener("submit", handleSubmit);