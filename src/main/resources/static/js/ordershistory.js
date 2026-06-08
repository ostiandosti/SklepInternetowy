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

            let itemsHtml = "";
            let calcTotal = 0;

            if (order.items && order.items.length > 0) {
                order.items.forEach(item => {
                    const itemTotal = item.price * item.quantity;
                    calcTotal += itemTotal;

                    itemsHtml += `
                        <div class="order-item">
                            <span>${item.productName}</span>
                            <span>${item.quantity}x</span>
                            <span>${item.price.toFixed(2)} zł</span>
                            <span>${itemTotal.toFixed(2)} zł</span>
                        </div>
                    `;
                });
            }

            container.innerHTML += `
                <div class="order-card">

                    <div class="order-header">
                        <span>Zamówienie #${order.id}</span>
                        <span class="status ${order.status}">
                            ${order.status}
                        </span>
                    </div>

                    <p><strong>Data:</strong> ${new Date(order.createdAt).toLocaleString()}</p>

                    <div class="order-item header">
                        <span>Produkt</span>
                        <span>Ilość</span>
                        <span>Cena</span>
                        <span>Suma</span>
                    </div>

                    <div class="order-items">
                        ${itemsHtml}
                    </div>

                    <hr>

                    <p><strong>Suma:</strong> ${order.totalPrice ?? calcTotal} zł</p>

                </div>
            `;
        });

    } catch (err) {
        console.error(err);
        container.innerHTML = "<h2>Błąd ładowania zamówień</h2>";
    }
}
document.addEventListener("DOMContentLoaded", loadOrders);