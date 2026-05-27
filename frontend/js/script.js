function loadProducts() {

  fetch("http://localhost:8080/products/get")
    .then(response => response.json())
    .then(products => {

      const list = document.getElementById("list");
      list.innerHTML = "";

      products.forEach(p => {
        list.innerHTML += `
          <div class="product">

            <h3>${p.name}</h3>
            <p>${p.description || ""}</p>
            <p>Cena: ${p.price} zł</p>
            <p>Ilość: ${p.quantity}</p>

            <img src="${p.imageUrl || ""}" width="80">

          </div>
        `;
      });

    })
    .catch(error => {
      console.error("Błąd pobierania produktów:", error);
    });
}