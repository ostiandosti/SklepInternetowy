const API = "http://localhost:8080";

function getToken() {
    return localStorage.getItem("token");
}

function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}


async function loadProducts() {
    const response = await fetch(API + "/products/get");
    const products = await response.json();
    const container = document.getElementById("products");

    products.forEach(product => {
        container.innerHTML += `
            <div class="product">
                <h2>${product.name}</h2>
                <img src="${API}/pictures/${product.imageUrl}" alt="${product.name}">
                <p><strong>Opis:</strong> ${product.description}</p>
                <p><strong>Cena:</strong> ${product.price} zł</p>
                <p><strong>Dostępne:</strong> ${product.quantity} szt.</p>
                <button class="add-to-cart" onclick="addToCart(${product.id})">
                    Dodaj do koszyka
                </button>
            </div>
        `;
    });
}
loadProducts();

document.querySelector(".cart-btn").addEventListener("click", () => {
    window.location.href = "cart.html";
});

// -------------------------------------------------------
// Dodaj produkt do koszyka
// Wysyła POST /cart/add z id produktu i ilością = 1
// -------------------------------------------------------
async function addToCart(productId) {
    // Sprawdź czy user jest zalogowany
    if (!getToken()) {
        alert("Musisz się zalogować żeby dodać do koszyka!");
        window.location.href = "login.html";
        return;
    }

    const response = await fetch(API + "/cart/add", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
            productId: productId,
            quantity: 1          // zawsze dodajemy 1 sztukę
        })
    });

    if (response.ok) {
        alert("Dodano do koszyka!");
    } else {
        const msg = await response.text();
        alert("Błąd: " + msg); // np. brak w magazynie
    }
}

// =========================
// AUTH SYSTEM
// =========================

function updateAuthUI() {
    const user = localStorage.getItem("user");
    const authBtn = document.getElementById("authBtn");
    const avatar = document.querySelector(".avatar");

    if (user) {
        authBtn.innerText = "Wyloguj się";
        authBtn.href = "#";

        avatar.innerText = "👤 " + user.split("@")[0];

        authBtn.addEventListener("click", (e) => {
            e.preventDefault();

            localStorage.removeItem("user");
            window.location.href = "/login.html";
        });

    } else {
        authBtn.innerText = "Zaloguj się";
        authBtn.href = "/login.html";

        avatar.innerText = "👤";
    }
}

updateAuthUI();