// Mostrar productos de la cesta
function loadShoppingCart() {
	// Obtener el carrito de la cesta del localStorage
	let cartLfd = JSON.parse(localStorage.getItem('cartLfd')) || [];

	// Redireccionar a pagina principal si el carrito esta vacio
	if (cartLfd.length === 0) {
		document.getElementById('bodyModalPay').textContent = "¡Oops! Parece que el carrito está vacío. ¿Por qué no te das una vuelta por nuestra tienda y descubres nuestros increíbles productos? Estamos seguros de que encontrarás algo que te encantará.";
		document.getElementById("hrefModalPay").onclick = function() {
			window.location.href = "/";
		};

		$('#modalPay').modal('show');
	} else {
		// Maquetar tabla con productos
		layoutTableCar(cartLfd);
	}


}

// Maquetar tabla con productos
async function layoutTableCar(cartLfd) {
	let tbody = document.getElementById('tbodyProductsCar');

	for (let productCar of cartLfd) {
		let product = await searchProductById(productCar.productId);

		let tr = document.createElement('tr');
		tr.id = `productRow_${productCar.productId}`;

		// Columna Imagen
		let tdImage = document.createElement('td');
		let aImage = document.createElement('a');
		aImage.href = '#';
		aImage.onclick = () => {
			// Mostrar Modal con Product
			showModalProduct(product);
		};
		let img = document.createElement('img');
		img.classList.add('img-thumbnail');
		img.style.width = "100px";
		img.src = 'data:image/jpeg;base64,' + product.images[0];
		aImage.append(img);
		tdImage.append(aImage);
		// Annadir a la fila
		tr.append(tdImage);

		// Columna Nombre de producto
		let tdName = document.createElement('td');
		tdName.classList.add('align-middle');
		let aName = document.createElement('a');
		aName.href = '#';
		aName.onclick = () => {
			// Mostrar Modal con Product
			showModalProduct(product);
		};
		aName.textContent = product.productName;
		tdName.append(aName);
		// Annadir a la fila
		tr.append(tdName);

		// Columna Cantidad de producto
		let tdQuantity = document.createElement('td');
		tdQuantity.classList.add('align-middle');
		let input = document.createElement('input');
		input.id = `inputQuantity_${productCar.productId}`;
		input.value = productCar.quantity;
		input.style.width = "50px";
		input.type = "number";
		input.min = 1;
		input.max = 100;
		input.addEventListener('change', function() {
			// Actualizar la cantidad en el array cartLfd
			productCar.quantity = parseInt(this.value);

			// Actualizar valores en la fila
			let tdUnityPrice = document.getElementById(`tdUnityPrice_${productCar.productId}`).textContent;
			document.getElementById(`tdTotalPrice_${productCar.productId}`).textContent = tdUnityPrice * productCar.quantity;
			localStorage.setItem('cartLfd', JSON.stringify(cartLfd));

			// Calcular total del carrito
			carTotal(cartLfd);

			// Actualizar cantidad en carrito
			addCart();
		});
		tdQuantity.append(input);
		// Annadir a la fila
		tr.append(tdQuantity);

		// Columna Precio unitario
		let tdUnityPrice = document.createElement('td');
		tdUnityPrice.classList.add('align-middle');
		tdUnityPrice.id = `tdUnityPrice_${productCar.productId}`;
		tdUnityPrice.textContent = product.pvpPrice + '€';
		// Annadir a la fila
		tr.append(tdUnityPrice);

		// Columna Precio total
		let tdTotalPrice = document.createElement('td');
		tdTotalPrice.classList.add('align-middle');
		tdTotalPrice.id = `tdTotalPrice_${productCar.productId}`;
		tdTotalPrice.textContent = product.pvpPrice * productCar.quantity + '€';
		// Annadir a la fila
		tr.append(tdTotalPrice);

		// Columna eliminar
		let tdDelete = document.createElement('td');
		tdDelete.classList.add('align-middle');
		let button = document.createElement('button');
		button.classList.add('btn', 'btn-danger');
		let i = document.createElement('i');
		i.classList.add('fas', 'fa-trash');
		button.append(i);
		button.onclick = () => {
			// Eliminar la fila de la tabla
			tr.remove();
			// Eliminar el elemento correspondiente del arreglo cartLfd
			let index = cartLfd.findIndex(item => item.productId === productCar.productId);
			if (index !== -1) {
				cartLfd.splice(index, 1);
			}

			localStorage.setItem('cartLfd', JSON.stringify(cartLfd));
			addCart();
		};
		tdDelete.append(button);
		// Annadir a la fila
		tr.append(tdDelete);

		tbody.append(tr);
	}

	// Calcular total del carrito
	carTotal(cartLfd);
}

// Calcular total del carrito
function carTotal(cartLfd) {
	// Obtener todos los elementos tdTotalPrice y sumar sus valores
	let total = 0;
	for (let productCar of cartLfd) {
		let totalPriceElement = document.getElementById(`tdTotalPrice_${productCar.productId}`);
		if (totalPriceElement) {
			total += parseFloat(totalPriceElement.textContent);
		}
	}

	// Establecer el total en el h4
	document.getElementById("carTotal").textContent = total.toFixed(2) + '€'; // Asegura que el total tenga dos decimales
}

// Buscar producto por ID
async function searchProductById(productId) {
	try {
		let response = await fetch("/products/searchById?productId=" + productId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.product.productId) {
			return data.product;
		} else {
			window.location.href = urlError;
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}