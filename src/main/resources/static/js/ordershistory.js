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
// LOAD ORDERS
// =========================

async function loadOrders() {
    const container = document.getElementById("orders");

    if (!getToken()) {
        container.innerHTML = "<h2>Musisz się zalogować</h2>";
        return;
    }

    try {
        const response = await fetch(API + "/orders/history", {
            headers: authHeaders()
        });

        const orders = await response.json();

        if (!orders.length) {
            container.innerHTML = "<h2>Brak zamówień</h2>";
            return;
        }

        container.innerHTML = "";

        orders.forEach(order => {
            container.innerHTML += `
                <div class="order-card">
                    <div class="order-header">
                        <span>Zamówienie #${order.id}</span>
                        <span class="status ${order.status}">
                            ${order.status}
                        </span>
                    </div>

                    <p><strong>Data:</strong> ${new Date(order.createdAt).toLocaleString()}</p>
                    <p><strong>Kwota:</strong> ${order.totalPrice} zł</p>
                </div>
            `;
        });

    } catch (err) {
        console.error(err);
        container.innerHTML = "<h2>Błąd ładowania zamówień</h2>";
    }
}

document.addEventListener("DOMContentLoaded", loadOrders);