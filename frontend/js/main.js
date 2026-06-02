
    async function loadProducts() {

        const response = await fetch("http://localhost:8080/products/get");

        const products = await response.json();

        const container = document.getElementById("products");

        products.forEach(product => {

            container.innerHTML += `
                <div class="product">
                    <h2>${product.name}</h2>

                    <img src="http://localhost:8080/pictures/${product.imageUrl}" alt="${product.name}">

                    <p><strong>Opis:</strong> ${product.description}</p>

                    <p><strong>Cena:</strong> ${product.price} zł</p>

                    <button class="add-to-cart" data-id="${product.id}">
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