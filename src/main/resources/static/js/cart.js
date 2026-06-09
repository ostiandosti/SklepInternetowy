// ============================================================
// cart.js — obsługa koszyka
// ============================================================
// Jak to działa w skrócie:
//   1. Pobieramy token JWT z localStorage (user musi być zalogowany)
//   2. Każde żądanie do /cart/* wysyłamy z tym tokenem w nagłówku
//   3. Backend sprawdza token i wie kto pyta
// ============================================================

const API = "http://localhost:8080";

// Pobieramy token który zapisałeś podczas logowania
function getToken() {
    return localStorage.getItem("token");
}

// Pomocnicza funkcja: buduje nagłówki z tokenem
// Backend wymaga: Authorization: Bearer <token>
function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}

// -------------------------------------------------------
// Sprawdź czy user jest zalogowany
// Jeśli nie ma tokena → wróć do strony logowania
// -------------------------------------------------------
if (!getToken()) {
    alert("Musisz się zalogować!");
    window.location.href = "login.html";
}

// -------------------------------------------------------
// Pobierz koszyk z backendu i wyświetl
// -------------------------------------------------------
async function loadCart() {
    const response = await fetch(API + "/cart", {
        headers: authHeaders()
    });

    // 401 Unauthorized → token wygasł lub błędny
    if (response.status === 401) {
        alert("Sesja wygasła, zaloguj się ponownie");
        window.location.href = "login.html";
        return;
    }

    const items = await response.json();
    items.sort((a, b) => a.id - b.id);
    renderCart(items);
}

// -------------------------------------------------------
// Narysuj koszyk na stronie
// -------------------------------------------------------
function renderCart(items) {
    const container = document.getElementById("cart-items");
    container.innerHTML = "";

    if (items.length === 0) {
        container.innerHTML = "<p>Koszyk jest pusty.</p>";
        updateSummary([]);
        return;
    }

    items.forEach(item => {
        // Dla każdej pozycji tworzymy HTML
        const div = document.createElement("div");
        div.className = "item";
        div.dataset.price = item.price;

        div.innerHTML = `
            <div class="item-info">
                <h3>${item.productName}</h3>
                <p class="price">${item.price.toFixed(2).replace(".", ",")} zł</p>
            </div>

            <div class="quantity">
                <button class="minus" onclick="changeQuantity(${item.id}, ${item.quantity - 1})">−</button>
                <span class="count">${item.quantity}</span>
                <button class="plus"  onclick="changeQuantity(${item.id}, ${item.quantity + 1})">+</button>
            </div>

            <button class="remove" onclick="removeItem(${item.id})">Usuń</button>
        `;

        container.appendChild(div);
    });

    updateSummary(items);
}

// -------------------------------------------------------
// Przelicz i wyświetl podsumowanie (łączna cena, ilość)
// -------------------------------------------------------
function updateSummary(items) {
    let total = 0;
    let count = 0;

    items.forEach(item => {
        total += item.price * item.quantity;
        count += item.quantity;
    });

    document.getElementById("items-count").textContent = count;
    document.getElementById("total-price").textContent =
        (total + 15).toFixed(2).replace(".", ",") + " zł"; // +15 zł dostawa
}

// -------------------------------------------------------
// Zmień ilość produktu
// Wywołuje PUT /cart/update/{itemId}
// -------------------------------------------------------
async function changeQuantity(itemId, newQuantity) {
    // Jeśli ilość = 0, pytamy czy usunąć
    if (newQuantity <= 0) {
        removeItem(itemId);
        return;
    }

    const response = await fetch(API + "/cart/update/" + itemId, {
        method: "PUT",
        headers: authHeaders(),
        body: JSON.stringify({ quantity: newQuantity })
    });

    if (response.ok) {
        loadCart(); // Odśwież widok koszyka
    } else {
        const msg = await response.text();
        alert("Błąd: " + msg); // np. "Brak wystarczającej ilości w magazynie"
    }
}

// -------------------------------------------------------
// Usuń jeden produkt
// Wywołuje DELETE /cart/remove/{itemId}
// -------------------------------------------------------
async function removeItem(itemId) {
    const response = await fetch(API + "/cart/remove/" + itemId, {
        method: "DELETE",
        headers: authHeaders()
    });

    if (response.ok) {
        loadCart(); // Odśwież
    } else {
        alert("Błąd przy usuwaniu");
    }
}

// -------------------------------------------------------
// Wyczyść cały koszyk
// Wywołuje DELETE /cart/clear
// -------------------------------------------------------
async function clearCart() {
    if (!confirm("Czy na pewno chcesz wyczyścić koszyk?")) return;

    const response = await fetch(API + "/cart/clear", {
        method: "DELETE",
        headers: authHeaders()
    });

    if (response.ok) {
        loadCart(); // Odśwież (będzie pusty)
    } else {
        alert("Błąd przy czyszczeniu koszyka");
    }
}

async function placeOrder() {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Musisz się zalogować!");
        window.location.href = "login.html";
        return;
    }

    const response = await fetch("http://localhost:8080/orders/place", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
    });

    const text = await response.text();

    if (response.ok) {
        alert("Zamówienie złożone!");

        // 🔥 KLUCZ: odśwież koszyk / stronę
        window.location.reload();

        // albo alternatywnie:
        // loadCart(); // jeśli jesteś na cart.html
    } else {
        alert("Błąd: " + text);
    }
}
// Uruchom gdy strona się załaduje
loadCart();