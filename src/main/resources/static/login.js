const form = document.getElementById('login');
const user = document.getElementById('username');
const pass = document.getElementById('password');

const headers = {
    'Content-Type': 'application/json'
};

const handleSubmit = async(e) => {
    e.preventDefault();

    let bodyObj = {
        username: user.value,
        password: pass.value
    };

    const response = await fetch("http://localhost:8080/api/v1/users/login", {
        method: "POST",
        body: JSON.stringify(bodyObj),
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        window.location.replace(await response.valueOf().text());
    };
}

form.addEventListener("submit", handleSubmit);