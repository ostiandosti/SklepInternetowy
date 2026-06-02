console.log("Strona logowania załadowana");

document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const data = await response.text();

        document.getElementById("result").innerText = data;

    } catch (error) {
        console.error(error);
        document.getElementById("result").innerText = "Błąd połączenia";
    }
});
