const container = document.getElementById("products-container");

async function loadProducts() {

    try {

        const response = await fetch("http://localhost:8080/products/get");
        const products = await response.json();

        container.innerHTML = "";

        products.forEach(product => {

            container.innerHTML += `
                <div class="product-card">
                    <h3>${product.name}</h3>

                    <p>${product.description ?? ""}</p>

                    <p class="price">
                        ${product.price} zł
                    </p>

                    <button
                        class="delete-btn"
                        onclick="deleteProduct(${product.id})">
                        Usuń
                    </button>
                </div>
            `;
        });

    } catch (error) {

        container.innerHTML =
            "<p>Nie udało się pobrać produktów.</p>";

        console.error(error);
    }
}

async function deleteProduct(id) {

    if (!confirm("Usunąć produkt?")) {
        return;
    }

    try {

        await fetch(
            `http://localhost:8080/products/${id}`,
            {
                method: "DELETE"
            }
        );

        loadProducts();

    } catch (error) {
        console.error(error);
    }
}

loadProducts();