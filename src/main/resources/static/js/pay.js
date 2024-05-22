// Pagar
function pay() {
	let active = false;

	// Obtener la lista de direcciones
	let addressesList = document.getElementById('addresses');

	// Obtener todos los elementos dentro de la lista
	let addressItems = addressesList.querySelectorAll('.list-group-item');

	if (addressItems) {
		// Iterar sobre los elementos para encontrar el elemento activo
		addressItems.forEach(async item => {
			if (item.classList.contains('active')) {
				active = true;

				// Crear Orden pendiente de pago y guardar direccion
				let orderDto = await newOrderPendingPayment(item.id);

				// DEV
				// ENVIAR PETICION A PAYPAL
				// *******************************************************************************************************************
				// Si ha pagado
				let pagoRealizado = true;
				if (pagoRealizado) {
					// Guardar orden
					let confirmOrder = await saveOrder(orderDto);

					// Limpiar carrito
					if (confirmOrder) {
						localStorage.removeItem('cartLfd');
						localStorage.removeItem('moneyToPay');
						localStorage.removeItem('pendingOrder');

						document.getElementById('bodyModalPay').textContent = "¡La compra se ha realizado correctamente!";
						document.getElementById("hrefModalPay").onclick = function() {
							window.location.href = "/";
						};

						$('#modalPay').modal('show');
					}
				} else {
					document.getElementById('bodyModalPay').textContent = "¡Upss!, parece que el pago no se ha realizado.";

					$('#modalPay').modal('show');
				}
			}
		});
	}

	if (!active) {
		document.getElementById('bodyModalPay').textContent = "Debe seleccionar una dirección de envío antes de proceder al pago";
		$('#modalPay').modal('show');
	}
}

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

// Crear Orden pendiente de pago y guardar direccion
async function newOrderPendingPayment(addressId) {
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
	};

	localStorage.setItem('pendingOrder', JSON.stringify(orderDto));

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
	document.getElementById("moneyToPay").textContent = moneyToPay;

	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Obtener direcciones por usuario
	let addresses = await searchPostalAddressByUser(user.userId);

	if (addresses === null || addresses.length === 0) {
		let message = "No hay registrada ninguna dirección de envío.";
		showMessage(document.getElementById("messageNotFoundAddresses"), message)

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

	for (const address of addresses) {

		let aAddress = document.createElement('a');
		aAddress.classList.add('list-group-item', 'list-group-item-action', 'item-address');
		aAddress.id = address.addressId;
		if (lastAddress == address.addressId) {
			aAddress.classList.add('active');
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
		};
		aAddress.append(address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country);

		addressesList.append(aAddress);
	}
}

// API PayPal
window.paypal
	.Buttons({
		style: {
			shape: "rect",
			layout: "vertical",
			color: "gold",
			label: "paypal",
		},
		async createOrder() {
			try {
				const response = await fetch("/api/orders", {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
					// use the "body" param to optionally pass additional order information
					// like product ids and quantities
					body: JSON.stringify({
						cart: [
							{
								id: "YOUR_PRODUCT_ID",
								quantity: "YOUR_PRODUCT_QUANTITY",
							},
						],
					}),
				});

				const orderData = await response.json();

				if (orderData.id) {
					return orderData.id;
				}
				const errorDetail = orderData?.details?.[0];
				const errorMessage = errorDetail
					? `${errorDetail.issue} ${errorDetail.description} (${orderData.debug_id})`
					: JSON.stringify(orderData);

				throw new Error(errorMessage);
			} catch (error) {
				console.error(error);
				// resultMessage(`Could not initiate PayPal Checkout...<br><br>${error}`);
			}
		},
		async onApprove(data, actions) {
			try {
				const response = await fetch(`/api/orders/${data.orderID}/capture`, {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
				});

				const orderData = await response.json();
				// Three cases to handle:
				//   (1) Recoverable INSTRUMENT_DECLINED -> call actions.restart()
				//   (2) Other non-recoverable errors -> Show a failure message
				//   (3) Successful transaction -> Show confirmation or thank you message

				const errorDetail = orderData?.details?.[0];

				if (errorDetail?.issue === "INSTRUMENT_DECLINED") {
					// (1) Recoverable INSTRUMENT_DECLINED -> call actions.restart()
					// recoverable state, per
					// https://developer.paypal.com/docs/checkout/standard/customize/handle-funding-failures/
					return actions.restart();
				} else if (errorDetail) {
					// (2) Other non-recoverable errors -> Show a failure message
					throw new Error(`${errorDetail.description} (${orderData.debug_id})`);
				} else if (!orderData.purchase_units) {
					throw new Error(JSON.stringify(orderData));
				} else {
					// (3) Successful transaction -> Show confirmation or thank you message
					// Or go to another URL:  actions.redirect('thank_you.html');
					const transaction =
						orderData?.purchase_units?.[0]?.payments?.captures?.[0] ||
						orderData?.purchase_units?.[0]?.payments?.authorizations?.[0];
					resultMessage(
						`Transaction ${transaction.status}: ${transaction.id}<br>
          <br>See console for all available details`
					);
					console.log(
						"Capture result",
						orderData,
						JSON.stringify(orderData, null, 2)
					);
				}
			} catch (error) {
				console.error(error);
				resultMessage(
					`Sorry, your transaction could not be processed...<br><br>${error}`
				);
			}
		},
	})
	.render("#paypal-button-container");












