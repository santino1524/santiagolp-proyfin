// Loader
document.addEventListener("DOMContentLoaded", function() {
	// Activar loader
	loaderActive();

	// Esperar a que todo el contenido se cargue
	window.addEventListener("load", function() {
		// Desactivar loader
		loaderDeactivate();
	});
});

// Guardar orden
async function saveOrder(orderDto) {
	let data;

	try {
		let response = await fetch("/orders/save", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(orderDto)
		});


		if (response.status === 200) {
			return true;
		} else if (response.status === 400) {
			document.getElementById('bodyModalPay').textContent = "Ha ocurrido un error al registrar el pedido, contacte con el administrador de la tienda.";
			$('#modalPay').modal('show');

			return;
		} else if (response.status === 422) {
			data = await response.json();

			let message = 'Se han modificado las existencias de los productos: ';
			for (let product of data.products) {
				message += product.productName + ': ' + product.productQuantity + ' unidades, ';
			}
			message += 'ajusta las cantidades a comprar. Lo sentimos.';

			document.getElementById('bodyModalPay').textContent = message;
			document.getElementById("hrefModalPay").onclick = function() {
				window.location.href = "/shoppingCart";
			};
			$('#modalPay').modal('show');

			return;
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}

	if (data) {
		return [data.address];
	}
}

// Crear Orden y guardar direccion
async function newOrder(addressId, transactionId) {
	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);
	localStorage.setItem('lastAddress', JSON.stringify(addressId));
	// Obtener el carrito de la cesta del localStorage
	let soldProductsDto = JSON.parse(localStorage.getItem('cartLfd')) || [];
	// Obtener el total
	let total = JSON.parse(localStorage.getItem('moneyToPay'));
	// Eliminar el simbolo de euro de la cadena
	total = total.replace('€', '');

	orderDto = {
		orderNumber: generateEAN13(),
		orderDate: getDate(),
		total: parseFloat(total),
		status: "CREADO",
		userId: user.userId,
		soldProductsDto: soldProductsDto,
		addressId: addressId,
		transactionId: transactionId,
	};

	return orderDto;
}

// Obtener fecha
function getDate() {
	// Crear una nueva fecha en JavaScript
	let currentDate = new Date();

	// Obtener los componentes de la fecha
	let year = currentDate.getFullYear();
	let month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
	let day = currentDate.getDate().toString().padStart(2, '0');
	let hour = currentDate.getHours().toString().padStart(2, '0');
	let minutes = currentDate.getMinutes().toString().padStart(2, '0');
	let seconds = currentDate.getSeconds().toString().padStart(2, '0');

	// Crear una cadena de texto en formato ISO 8601
	return `${year}-${month}-${day}T${hour}:${minutes}:${seconds}`;
}

// Cargar pagina de Pago
async function loadPayPage() {
	// Obtener el total a pagar del localStorage
	let moneyToPay = JSON.parse(localStorage.getItem('moneyToPay')) || '0€';
	document.getElementById("moneyToPay").textContent = `Total a pagar: ${moneyToPay}`;

	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener direcciones por usuario
	let addresses = await searchPostalAddressByUser(user.userId);

	if (addresses === null || addresses.length === 0) {
		let message = "No hay registrada ninguna dirección de envío.";
		showMessage(document.getElementById("messageNotFoundAddresses"), message);

		return;
	} else {
		// Maquetar direcciones
		layoutAddresses(addresses);
	}
}

// Maquetar direcciones
function layoutAddresses(addresses) {
	let addressesList = document.getElementById("addresses");

	//Obtener direccion de la ultima compra
	let lastAddress = JSON.parse(localStorage.getItem('lastAddress'));

	let total = 0;
	let isActive = false;
	for (let address of addresses) {
		total++;

		let aAddress = document.createElement('a');
		aAddress.classList.add('list-group-item', 'list-group-item-action', 'item-address');
		aAddress.id = address.addressId;
		if (lastAddress == address.addressId) {
			aAddress.classList.add('active');

			isActive = true;

			// Mostrar metodos de pago
			document.getElementById("paymentMethods").classList.remove('d-none');
		}
		aAddress.href = '#';
		aAddress.onclick = (event) => {
			let clickedItem = event.target;
			let listItems = document.querySelectorAll('.list-group-item.item-address');

			// Desactivar todos los elementos del grupo
			listItems.forEach(otherItem => {
				otherItem.classList.remove('active');
			});

			// Activar el elemento en el que se hizo clic
			clickedItem.classList.add('active');

			// Mostrar metodos de pago
			document.getElementById("paymentMethods").classList.remove('d-none');
		};
		aAddress.append(address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country);

		addressesList.append(aAddress);
	}

	if (!isActive && total === 1) {
		let listItems = document.querySelectorAll('.list-group-item.item-address');

		if (listItems && listItems.length === 1) {
			listItems[0].classList.add('active');

			// Mostrar metodos de pago
			document.getElementById("paymentMethods").classList.remove('d-none');
		}
	}
}

// Validar orden
async function validateOrder(orderDto) {
	let data;

	try {
		let response = await fetch("/orders/validateOrder", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(orderDto)
		});


		if (response.status === 200) {
			return true;
		} else if (response.status === 400) {
			return false;
		} else if (response.status === 422) {
			data = await response.json();

			let message = 'Se han modificado las existencias de los productos: ';
			for (let product of data.products) {
				message += product.productName + ': ' + product.productQuantity + ' unidades, ';
			}
			message += 'ajusta las cantidades a comprar. Lo sentimos.';

			document.getElementById('bodyModalPay').textContent = message;
			document.getElementById("hrefModalPay").onclick = function() {
				window.location.href = "/shoppingCart";
			};
			$('#modalPay').modal('show');

			return;
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Integracion con Paypal
window.paypal
	.Buttons({
		style: {
			shape: "rect",
			layout: "horizontal",
			color: "gold",
			label: "pay",
		},
		createOrder: async (data, actions) => {

			// Obtener la lista de direcciones
			let addressesList = document.getElementById('addresses');

			// Obtener todos los elementos dentro de la lista
			let addressItems = addressesList.querySelectorAll('.list-group-item');

			let isValid = false;

			if (addressItems) {
				// Iterar sobre los elementos para encontrar el elemento activo
				for (let item of addressItems) {
					if (item.classList.contains('active')) {

						// Crear Orden pendiente de pago y guardar direccion
						let orderDto = await newOrder(item.id);

						// Validar orden
						isValid = await validateOrder(orderDto);

						break;
					}
				}

				if (isValid === false) {
					integrityViolation();

					// Rechazar la transaccion
					return actions.reject();
				}
			}

			let moneyToPay = JSON.parse(localStorage.getItem('moneyToPay'));

			if (!moneyToPay) {
				integrityViolation();

				// Rechazar la transaccion
				return actions.reject();
			}

			// Eliminar el simbolo "€"
			let units = parseFloat(moneyToPay.replace("€", ""));

			return actions.order.create({
				purchase_units: [{
					amount: {
						value: units
					}
				}]
			});
		},
		onApprove: (data, actions) => {
			return actions.order.capture().then(async function(orderData) {
				// Obtener la lista de direcciones
				let addressesList = document.getElementById('addresses');

				// Obtener todos los elementos dentro de la lista
				let addressItems = addressesList.querySelectorAll('.list-group-item');

				if (addressItems) {
					// Iterar sobre los elementos para encontrar el elemento activo
					for (let item of addressItems) {
						if (item.classList.contains('active')) {

							// ID de transaccion
							let transactionId = orderData.purchase_units[0].payments.captures[0].id;

							// Crear Orden pendiente de pago y guardar dirección
							let orderDto = await newOrder(item.id, transactionId);

							// Guardar orden
							let confirmOrder = await saveOrder(orderDto);

							if (confirmOrder) {
								// Limpiar carrito
								localStorage.removeItem('cartLfd');
								localStorage.removeItem('moneyToPay');

								document.getElementById('bodyModalPay').textContent = "¡La compra se ha realizado correctamente! El ID de la transacción es " + transactionId;
								document.getElementById("hrefModalPay").onclick = function() {
									window.location.href = "/";
								};

								$('#modalPay').modal('show');

								break;
							}
						}
					}
				}
			});
		}
	}).render("#paypal-button-container");

// Accion para violacion de integridad de los datos de la orden
function integrityViolation() {
	// Ha ocurrido una violacion de seguridad
	document.getElementById('bodyModalPay').textContent = "Ha ocurrido un intento de violación de integridad en los datos";
	document.getElementById("hrefModalPay").onclick = function() {
		window.location.href = "/";
	};

	$('#modalPay').modal('show');

	// Limpiar carrito
	localStorage.removeItem('cartLfd');
	localStorage.removeItem('moneyToPay');
}