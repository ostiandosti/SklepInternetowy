let products = [];

function addProduct() {

    const p = {
        id: Date.now(),
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: document.getElementById("price").value,
        quantity: document.getElementById("quantity").value,
        image_url: document.getElementById("image").value
    };

    products.push(p);

    render();

    document.getElementById("name").value = "";
    document.getElementById("description").value = "";
    document.getElementById("price").value = "";
    document.getElementById("quantity").value = "";
    document.getElementById("image").value = "";
}

function render() {

    const list = document.getElementById("list");

    list.innerHTML = "";

    products.forEach(p => {

        list.innerHTML += `
            <div class="product">

                <input
                    class="edit-input"
                    value="${p.name}"
                    onchange="update(${p.id}, 'name', this.value)"
                >

                <textarea
                    class="edit-input"
                    onchange="update(${p.id}, 'description', this.value)"
                >${p.description}</textarea>

                <input
                    class="edit-input"
                    type="number"
                    value="${p.price}"
                    onchange="update(${p.id}, 'price', this.value)"
                >

                <input
                    class="edit-input"
                    type="number"
                    value="${p.quantity}"
                    onchange="update(${p.id}, 'quantity', this.value)"
                >

                <input
                    class="edit-input"
                    value="${p.image_url}"
                    onchange="update(${p.id}, 'image_url', this.value)"
                >

                <img src="${p.image_url}" alt="Produkt">

            </div>
        `;
    });
}

function update(id, field, value) {

    const product = products.find(
        p => p.id === id
    );

    if (product) {
        product[field] = value;
    }

    render();
}