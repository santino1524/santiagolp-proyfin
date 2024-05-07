// Cargar pagina de los pedidos de usuarios
async function loadOrdersPage() {
	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener ultimo pedido
	orders = await lastOrder(user.userId);

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

	for (let order of orders) {

		// Contenedor card 
		let divCard = document.createElement('div');
		divCard.classList.add('card');

		// Contenedor card body 
		let divCardBody = document.createElement('div');
		divCardBody.classList.add('card-body');

		// Titulo
		let h5Title = document.createElement('h5');
		h5Title.classList.add('card-title');
		h5Title.textContent = "Pedido " + order.orderNumber + " | " + order.orderDate;
		divCardBody.append(h5Title);

		// Estado
		let pStatus = document.createElement('p');
		pStatus.classList.add('card-text');
		pStatus.textContent = order.status;
		divCardBody.append(pStatus);

		// hr
		let hr = document.createElement('hr');
		divCardBody.append(hr);

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
		divCardBody.append(hr);

		// Factura
		let h6Bill = document.createElement('h6');
		h6Address.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h6Address.textContent = "Factura:";
		divCardBody.append(h6Bill);
		let pBill = document.createElement('p');
		pBill.classList.add('card-text');
		pBill.textContent = user.name + ' ' + user.surname + ' ' + user.secondSurname + ', ' + user.dni + ', ' + address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country;
		divCardBody.append(pBill);
		divCardBody.append(hr);

		// Pedido
		let h6Order = document.createElement('h6');
		h6Order.classList.add('card-subtitle', 'mb-2', 'text-muted');
		h6Order.textContent = "Resumen del pedido";
		divCardBody.append(h6Order);
		let ul = document.createElement('ul');
		ul.classList.add('list-group');
		// Productos
		for (let productSold of order.soldProductsDto) {

			let li = document.createElement('ul');
			li.classList.add('list-group-item', 'd-flex justify', 'align-items-center');
			let img = document.createElement('img');
			img.classList.add('img-thumbnail', 'mr-2');
			img.style.width = "50px";
			
			//buscar producto por productId del productSold para imagen, nombre, cantidad y precio
			
			img.src = 'data:image/jpeg;base64,' + product.images[0];

			//realizar el append correcto al div
		}

	}
}

// Mostrar todos los pedidos del usuario desc
async function showAllOrdersByUser() {
	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener lista de pedidos del usuario
	orders = await listOrdersDesc(user.userId);

	//Maquetar
	layoutOrders(orders);
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