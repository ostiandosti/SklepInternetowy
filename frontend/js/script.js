const API = "http://localhost:8080/products";

/* ---------------------------
   POBIERANIE PRODUKTÓW
----------------------------*/
function loadProducts() {
  fetch(API + "/get")
    .then(res => res.json())
    .then(data => render(data))
    .catch(err => console.error("Błąd GET:", err));
}

/* ---------------------------
   DODAWANIE PRODUKTU
----------------------------*/
function addProduct() {

  const product = {
    name: document.getElementById("name").value,
    description: document.getElementById("description").value,
    price: document.getElementById("price").value,
    quantity: document.getElementById("quantity").value,
    image_url: document.getElementById("image").value
  };

  fetch(API, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(product)
  })
  .then(() => {
    clearForm();
    loadProducts();
  })
  .catch(err => console.error("Błąd POST:", err));
}

/* ---------------------------
   USUWANIE
----------------------------*/
function deleteProduct(id) {

  fetch(`${API}/${id}`, {
    method: "DELETE"
  })
  .then(() => loadProducts())
  .catch(err => console.error("Błąd DELETE:", err));
}

/* ---------------------------
   EDYCJA (PUT)
----------------------------*/
function updateProduct(id, field, value, product) {

  product[field] = value;

  fetch(`${API}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(product)
  })
  .then(() => loadProducts())
  .catch(err => console.error("Błąd PUT:", err));
}

/* ---------------------------
   RENDER LISTY
----------------------------*/
function render(products) {

  const list = document.getElementById("list");
  list.innerHTML = "";

  products.forEach(p => {

    list.innerHTML += `
      <div class="product">

        <input value="${p.name}"
          onchange='updateProduct(${p.id}, "name", this.value, ${JSON.stringify(p).replaceAll("'", "&apos;")})'
        >

        <textarea
          onchange='updateProduct(${p.id}, "description", this.value, ${JSON.stringify(p).replaceAll("'", "&apos;")})'
        >${p.description || ""}</textarea>

        <input type="number" value="${p.price}"
          onchange='updateProduct(${p.id}, "price", this.value, ${JSON.stringify(p).replaceAll("'", "&apos;")})'
        >

        <input type="number" value="${p.quantity}"
          onchange='updateProduct(${p.id}, "quantity", this.value, ${JSON.stringify(p).replaceAll("'", "&apos;")})'
        >

        <input value="${p.image_url || ""}"
          onchange='updateProduct(${p.id}, "image_url", this.value, ${JSON.stringify(p).replaceAll("'", "&apos;")})'
        >

        <img src="${p.image_url || ""}">

        <button onclick="deleteProduct(${p.id})">
          Usuń
        </button>

      </div>
    `;
  });
}

/* ---------------------------
   CLEAR FORM
----------------------------*/
function clearForm() {
  document.getElementById("name").value = "";
  document.getElementById("description").value = "";
  document.getElementById("price").value = "";
  document.getElementById("quantity").value = "";
  document.getElementById("image").value = "";
}

/* ---------------------------
   START
----------------------------*/
loadProducts();
loadProducts();
loadProducts();