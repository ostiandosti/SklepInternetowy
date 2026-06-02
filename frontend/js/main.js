
    async function loadProducts() {

        const response = await fetch("http://localhost:8080/products/get");

        const products = await response.json();

        const container = document.getElementById("products");

        products.forEach(product => {

            container.innerHTML += `
                <div class="product">
                    <h2>${product.name}</h2>

                    <img src="${product.imageUrl}" alt="${product.name}">

                    <p><strong>Opis:</strong> ${product.description}</p>

                    <p><strong>Cena:</strong> ${product.price} zł</p>

                    <p><strong>Ilość:</strong> ${product.quantity}</p>

                    <p><strong>Kategoria:</strong> ${product.category}</p>
                </div>
            `;
        });
    }

    loadProducts();
