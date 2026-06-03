document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    clearFieldErrors();

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    let hasError = false;

    if (!username) {
        setFieldError("usernameError", "Podaj nazwę użytkownika");
        hasError = true;
    }

    if (!email) {
        setFieldError("emailError", "Podaj email");
        hasError = true;
    }

    if (!password) {
        setFieldError("passwordError", "Podaj hasło");
        hasError = true;
    }

    if (hasError) return;

    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email, password })
        });

        const data = await response.json().catch(() => null);

        if (!response.ok) {
            setFieldError("emailError", data?.error || "Błąd rejestracji");
            return;
        }

        window.location.href = "/login.html";

    } catch (error) {
        setFieldError("emailError", "Błąd połączenia z serwerem");
    }
});