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

// =========================
// PRODUCTS
// =========================

async function loadProducts() {
    const container = document.getElementById("products");
    if (!container)
        return;

    try {
        const response = await fetch(API + "/products/get");
        const products = await response.json();
        products.sort((a, b) => a.id - b.id);

        container.innerHTML = "";

        products.forEach(product => {
            container.innerHTML += `
<div class="product"
     style="--bg-image:url('${API}/pictures/${product.imageUrl}')">

    <div class="product-image">
        <img src="${API}/pictures/${product.imageUrl}" alt="${product.name}">
    </div>

    <div class="product-content">
        <h2>${product.name}</h2>
        <p><strong>Opis:</strong> ${product.description}</p>
        <p><strong>Cena:</strong> ${product.price} zł</p>
        <p><strong>Dostępne:</strong> ${product.quantity} szt.</p>

        <button class="add-to-cart"
                onclick="addToCart(${product.id})">
            Dodaj do koszyka
        </button>
    </div>

</div>
`;
        });

    } catch (err) {
        console.error("Błąd ładowania produktów:", err);
    }
}

// =========================
// CART
// =========================

async function addToCart(productId) {
    if (!getToken()) {
        alert("Musisz się zalogować żeby dodać do koszyka!");
        window.location.href = "/login.html";
        return;
    }

    try {
        const response = await fetch(API + "/cart/add", {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify({
                productId: productId,
                quantity: 1
            })
        });

        if (response.ok) {
            alert("Dodano do koszyka!");
        } else {
            const msg = await response.text();
            alert("Błąd: " + msg);
        }

    } catch (err) {
        console.error("Błąd koszyka:", err);
    }
}

// =========================
// AUTH UI
// =========================

function updateAuthUI() {
    const token = getToken();
    const authBtn = document.getElementById("authBtn");
    const avatar = document.querySelector(".avatar");

    if (!authBtn)
        return;

    if (token) {
        authBtn.innerText = "Wyloguj się";
        authBtn.href = "#";

        if (avatar)
            avatar.innerText = "👤";

        authBtn.onclick = (e) => {
            e.preventDefault();

            localStorage.removeItem("token");

            alert("Wylogowano pomyślnie");
            window.location.href = "/login.html";
        };

    } else {
        authBtn.innerText = "Zaloguj się";
        authBtn.href = "/login.html";

        if (avatar)
            avatar.innerText = "👤";

        authBtn.onclick = null;
    }
}

// =========================
// NAVIGATION
// =========================

function setupCartButton() {
    const btn = document.querySelector(".cart-btn");

    if (!btn)
        return;

    btn.addEventListener("click", () => {
        window.location.href = "/cart.html";
    });
}
function setupAvatarDropdown() {
    const avatar = document.getElementById("avatarBtn");
    const dropdown = document.getElementById("avatarDropdown");

    if (!avatar || !dropdown)
        return;

    avatar.addEventListener("click", (e) => {
        e.stopPropagation();
        dropdown.style.display =
                dropdown.style.display === "block" ? "none" : "block";
    });

    document.addEventListener("click", () => {
        dropdown.style.display = "none";
    });
}

// =========================
// INIT (NAJWAŻNIEJSZE)
// =========================

document.addEventListener("DOMContentLoaded", () => {
    loadProducts();
    updateAuthUI();
    setupCartButton();
    setupAvatarDropdown();
});