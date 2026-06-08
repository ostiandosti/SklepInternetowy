document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    clearFieldErrors();

    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    // Walidacja po stronie frontendu — żeby nie wysyłać pustych pól
    let hasError = false;
    if (!username) { setFieldError("usernameError", "Podaj nazwę użytkownika"); hasError = true; }
    if (!email) { setFieldError("emailError", "Podaj email"); hasError = true; }
    if (!password) { setFieldError("passwordError", "Podaj hasło"); hasError = true; }
    if (hasError) return;

    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email, password })
        });

        // response.json() czyta odpowiedź z serwera jako obiekt JS
        const data = await response.json().catch(() => null);

        if (!response.ok) {
            // Serwer zwrócił błąd np. { "error": "Email już istnieje" }
            setFieldError("emailError", data?.error || "Błąd rejestracji");
            return;
        }

        // Sukces — przenosimy na stronę logowania
        window.location.href = "frontend/login.html";

    } catch (error) {
        // Ten błąd oznacza że serwer w ogóle nie odpowiedział
        setFieldError("emailError", "Błąd połączenia z serwerem");
    }
});