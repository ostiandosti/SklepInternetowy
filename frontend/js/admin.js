let editingId = null;

const API = "http://localhost:8080/products";

// =====================
// LOAD
// =====================
async function loadProducts() {
    const res = await fetch(`${API}/get`);
    const products = await res.json();

    render(products);
    renderRecent();
}

// =====================
// ADD
// =====================
async function addProduct() {

    const product = {
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: document.getElementById("price").value,
        quantity: document.getElementById("quantity").value,
        imageUrl: document.getElementById("image").value
    };

    await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(product)
    });

    clearForm();
    loadProducts();
}

function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("description").value = "";
    document.getElementById("price").value = "";
    document.getElementById("quantity").value = "";
    document.getElementById("image").value = "";
}

// =====================
// RENDER
// =====================
function render(products) {

    const list = document.getElementById("list");
    list.innerHTML = "";

    products.forEach(p => {

        const edit = editingId === p.id;

        list.innerHTML += `
        <div class="product" id="product-${p.id}">

            <input value="${p.name}" ${!edit ? "disabled" : ""} id="name-${p.id}">
            <textarea ${!edit ? "disabled" : ""} id="desc-${p.id}">${p.description || ""}</textarea>

            <input type="number" value="${p.price}" ${!edit ? "disabled" : ""} id="price-${p.id}">
            <input type="number" value="${p.quantity}" ${!edit ? "disabled" : ""} id="qty-${p.id}">

            <input value="${p.imageUrl || ""}" ${!edit ? "disabled" : ""} id="img-${p.id}">

            <img src="http://localhost:8080/pictures/${p.imageUrl}" alt="${p.name}">

            <div style="margin-top:10px;">

                ${!edit
                ? `<button onclick="startEdit(${p.id})">Edytuj</button>`
                : `<button onclick="saveEdit(${p.id})">Zapisz</button>
                   <button onclick="cancelEdit()">Anuluj</button>`
            }

                <button onclick="deleteProduct(${p.id})">Usuń</button>
            </div>

        </div>
        `;
    });
}

// =====================
// EDIT
// =====================
function startEdit(id) {
    editingId = id;
    loadProducts();
}

function cancelEdit() {
    editingId = null;
    loadProducts();
}

// =====================
// SAVE (FIXED - ID INCLUDED)
// =====================
async function saveEdit(id) {

    const updated = {
        id: id, // 🔥 KLUCZOWY FIX
        name: document.getElementById(`name-${id}`).value,
        description: document.getElementById(`desc-${id}`).value,
        price: document.getElementById(`price-${id}`).value,
        quantity: document.getElementById(`qty-${id}`).value,
        imageUrl: document.getElementById(`img-${id}`).value
    };

    await fetch(`${API}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updated)
    });

    saveRecent(id, updated.name);

    editingId = null;
    loadProducts();
}

// =====================
// DELETE
// =====================
async function deleteProduct(id) {
    await fetch(`${API}/${id}`, {
        method: "DELETE"
    });

    loadProducts();
}

// =====================
// RECENT
// =====================
function saveRecent(id, name) {

    let recent = JSON.parse(localStorage.getItem("recent") || "[]");

    recent.unshift({
        id: Number(id),
        name: name,
        time: new Date().toLocaleTimeString()
    });

    recent = recent.slice(0, 5);

    localStorage.setItem("recent", JSON.stringify(recent));

    renderRecent();
}

function renderRecent() {

    const box = document.getElementById("recentBar");
    if (!box) return;

    const recent = JSON.parse(localStorage.getItem("recent") || "[]");

    box.innerHTML = recent.length
        ? recent.map(r => `
            <div class="recent-item" onclick="goToProduct(${r.id})">
                ${r.name} • ${r.time}
            </div>
        `).join("")
        : "🟡 brak ostatnich edycji";
}

// =====================
// GO TO PRODUCT
// =====================
function goToProduct(id) {

    const el = document.getElementById(`product-${id}`);
    if (!el) return;

    el.scrollIntoView({ behavior: "smooth", block: "center" });

    el.classList.add("highlight");

    setTimeout(() => el.classList.remove("highlight"), 1500);
}

// =====================
loadProducts();