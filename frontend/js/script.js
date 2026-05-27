function addProduct() {

  const product = {

    name:
      document.getElementById("name").value,

    description:
      document.getElementById("description").value,

    price:
      document.getElementById("price").value,

    quantity:
      document.getElementById("quantity").value,

    image_url:
      document.getElementById("image").value
  };

  console.log(product);

  document.getElementById("products").innerHTML += `
  
    <div class="product">

      <h3>${product.name}</h3>

      <p>${product.description}</p>

      <p>Cena: ${product.price} zł</p>

      <p>Ilość: ${product.quantity}</p>

      <img
        src="${product.image_url}"
        width="120"
      >

    </div>
  `;
}