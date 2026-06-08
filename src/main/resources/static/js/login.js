document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    clearFieldErrors();

    const email    = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    let hasError = false;
    if (!email)    { setFieldError("emailError",    "Podaj email");  hasError = true; }
    if (!password) { setFieldError("passwordError", "Podaj hasło"); hasError = true; }
    if (hasError) return;

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json().catch(() => null);

        if (!response.ok) {
            // Backend zwrócił { "error": "Nieprawidłowy email lub hasło" }
            setFieldError("emailError", data?.error || "Błędny email lub hasło");
            return;
        }

        // ✅ Serwer zwraca { "token": "eyJ..." }
        // Zapisujemy token — będziemy go wysyłać przy każdym kolejnym żądaniu
        localStorage.setItem("token", data.token);
        

        window.location.href = "/main.html";

    } catch (error) {
        setFieldError("emailError", "Błąd połączenia z serwerem");
    }
});