// Cargar pagina de los pedidos de usuarios
async function loadOrdersPage() {
	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener ultimo pedido
	let orders = await lastOrder(user.userId);

	//Maquetar
	layoutOrders(orders, user);
}

// Maquetar pedidos
async function layoutOrders(orders, user) {
	let divOrdersContainer = document.getElementById('ordersContainer');

	if (!orders) {
		document.getElementById('not-found').classList.remove('d-none');

		return;
	}

	let divCard;
	for (let order of orders) {

		// Contenedor card 
		divCard = document.createElement('div');
		divCard.classList.add('card', 'mb-4');

		// Contenedor card body 
		let divCardBody = document.createElement('div');
		divCardBody.classList.add('card-body');

		// Titulo
		let h5Title = document.createElement('h5');
		h5Title.classList.add('card-title');
		h5Title.textContent = "Pedido " + order.orderNumber + " | " + formatDate(order.orderDate);
		divCardBody.append(h5Title);

		// Estado
		let pStatus = document.createElement('p');
		pStatus.classList.add('card-text');
		pStatus.textContent = order.status;
		divCardBody.append(pStatus);

		// hr
		let hr1 = document.createElement('hr');
		divCardBody.append(hr1);

		// Datos de entrega
		let h5Delivery = document.createElement('h5');
		h5Delivery.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h5Delivery.textContent = "Datos de entrega";
		divCardBody.append(h5Delivery);

		// Direccion
		let h6Address = document.createElement('h6');
		h6Address.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h6Address.textContent = "Dirección:";
		divCardBody.append(h6Address);
		let pAddress = document.createElement('p');
		pAddress.classList.add('card-text');
		let address = await searchPostalAddressById(order.addressId);
		pAddress.textContent = address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country;
		divCardBody.append(pAddress);

		// Factura
		let h6Bill = document.createElement('h6');
		h6Bill.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h6Bill.textContent = "Factura:";
		divCardBody.append(h6Bill);
		let pBill = document.createElement('p');
		pBill.classList.add('card-text');
		pBill.textContent = user.name + ' ' + user.surname + ' ' + user.secondSurname + ', ' + user.dni + ', ' + address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country;
		divCardBody.append(pBill);

		// hr
		let hr2 = document.createElement('hr');
		divCardBody.append(hr2);

		// Pedido
		let h5Order = document.createElement('h5');
		h5Order.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h5Order.textContent = "Resumen del pedido";
		divCardBody.append(h5Order);
		let ul = document.createElement('ul');
		ul.classList.add('list-group');

		// Total pedido
		let sum = 0;

		// Productos
		for (let productSold of order.soldProductsDto) {
			//buscar producto por productId del productSold para imagen, nombre, cantidad y precio
			let product = await searchProductById(productSold.productId);

			// producto
			let li = document.createElement('li');
			li.classList.add('list-group-item', 'd-flex', 'justify', 'align-items-center');
			//Imagen
			let img = document.createElement('img');
			img.classList.add('img-thumbnail', 'mr-5');
			img.style.width = "50px";
			img.src = 'data:image/jpeg;base64,' + product.images[0];
			li.append(img);
			//Nombre de producto
			li.append(product.productName);
			//Cantidad
			let spanQuantity = document.createElement('span');
			spanQuantity.classList.add('badge', 'badge-primary', 'badge-pill', 'mr-5');
			spanQuantity.append('Cantidad: ' + productSold.quantity);
			li.append(spanQuantity);
			//Total
			let spanTotalProduct = document.createElement('span');
			spanTotalProduct.classList.add('badge', 'badge-success');
			let totalProduct = parseFloat(product.pvpPrice) * parseFloat(productSold.quantity)
			sum += totalProduct;
			spanTotalProduct.append(totalProduct + '€');
			li.append(spanTotalProduct);

			ul.append(li);
		}

		divCardBody.append(ul);

		// hr
		let hr3 = document.createElement('hr');
		divCardBody.append(hr3);

		let pTotal = document.createElement('p');
		pTotal.classList.add('lead');
		pTotal.append(sum.toFixed(2) + '€');
		divCardBody.append(pTotal);

		divCard.append(divCardBody);
		divOrdersContainer.append(divCard);
	}

	

	// Mostrar boton si se mostraron pedidos
	if (orders) {
		document.getElementById("showAllOrders").classList.remove('d-none');
	}

}

// Formatear fecha
function formatDate(orderDate){
// Crear un objeto Date a partir de la cadena de fecha y hora
let dataDate = new Date(orderDate);

// Obtener los componentes de la fecha
let day = dataDate.getDate();
let month = dataDate.getMonth() + 1;
let year = dataDate.getFullYear();

// Formatear la fecha
return `${day < 10 ? '0' + day : day}-${month < 10 ? '0' + month : month}-${year}`;
}

// Mostrar todos los pedidos del usuario desc
async function showAllOrdersByUser() {
	// Limpiar productos existentes
	document.getElementById("ordersContainer").innerHTML = '';

	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener lista de pedidos del usuario
	orders = await listOrdersDesc(user.userId);

	//Maquetar
	layoutOrders(orders, user);
}

// Obtener ultimo pedido por usuario
async function lastOrder(userId) {
	try {
		let response = await fetch("/orders/searchTopByUser?userId=" + userId, {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.orders;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Obtener pedidos por usuario ordenados desc
async function listOrdersDesc(userId) {
	try {
		let response = await fetch("/orders/searchByUserDateDesc?userId=" + userId, {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.orders;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}