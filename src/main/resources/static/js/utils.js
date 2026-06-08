
function setFieldError(id, message) {
    const el = document.getElementById(id);
    if (el) el.innerText = message;
}

function clearFieldErrors() {
    document.querySelectorAll(".field-error").forEach(el => {
        el.innerText = "";
    });
}

// Zwraca token z localStorage — używany przy każdym żądaniu do API
function getToken() {
    return localStorage.getItem("token");
}

// Wylogowanie — czyści token i przenosi na login
function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}