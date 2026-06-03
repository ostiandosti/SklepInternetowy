
function setFieldError(id, message) {
    const el = document.getElementById(id);
    if (el) el.innerText = message;
}

function clearFieldErrors() {
    document.querySelectorAll(".field-error").forEach(el => {
        el.innerText = "";
    });
}