document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    clearFieldErrors();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    let hasError = false;

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
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json().catch(() => null);

        if (!response.ok) {
            // 🔥 BŁĄD Z BACKENDU PRZY EMAIL
            setFieldError("emailError", data?.error || "Błędny email lub hasło");
            setFieldError("passwordError", "");
            return;
        }

        localStorage.setItem("user", email);

        window.location.href = "/main.html";

    } catch (error) {
        setFieldError("emailError", "Błąd połączenia z serwerem");
    }
});