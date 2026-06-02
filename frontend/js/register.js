const email = document.getElementById("email");
const error = document.getElementById("error");

email.addEventListener("input", function () {

    if (email.validity.valid) {
        error.style.display = "none";
    } else {
        error.style.display = "block";
    }

});

const password = document.getElementById("password");
const passError = document.getElementById("passError");

password.addEventListener("input", function () {

    const regex =
        /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.#_-]).{8,}$/;

    if (regex.test(password.value)) {
        passError.style.display = "none";
    } else {
        passError.style.display = "block";
    }

});