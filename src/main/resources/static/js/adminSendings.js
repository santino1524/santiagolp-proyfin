// Cargar nuevos pedidos
async function loadNewOrders() {
	await loadAlerts();
	let orders = await searchByCreado();

	if (orders && orders.length > 0) {
		document.getElementById("ordersFound").classList.remove('d-none');

		// Maquetar tabla
		await layoutTableOrders(orders);
	} else {
		document.getElementById("msgNotFound").classList.remove('d-none');
	}
}

// Maquetar tabla con pedidos a enviar
async function layoutTableOrders(orders) {
	let tbody = document.getElementById('tbodyOrders');

	// Limpiar el contenido del tbody
	tbody.innerHTML = '';

	for (let order of orders) {

		let tr = document.createElement('tr');
		tr.id = `orderRow_${order.orderId}`;
		tr.classList.add('data-row');

		// Columna numero de pedido
		let tdOrder = document.createElement('td');
		tdOrder.classList.add('align-middle');
		tdOrder.textContent = Number(order.orderNumber);
		// Annadir a la fila
		tr.append(tdOrder);

		// Columna fecha de pedido
		let tdDate = document.createElement('td');
		tdDate.classList.add('align-middle');
		tdDate.textContent = formatDate(order.orderDate);
		// Annadir a la fila
		tr.append(tdDate);

		// Columna Precio total
		let tdTotalPrice = document.createElement('td');
		tdTotalPrice.classList.add('align-middle');
		tdTotalPrice.textContent = order.total.toFixed(2) + '€';
		// Annadir a la fila
		tr.append(tdTotalPrice);

		// Columna procesar envio
		let tdPrint = document.createElement('td');
		tdPrint.classList.add('align-middle');
		tdPrint.id = `tdPrint_${order.orderId}`;
		let buttonPrint = document.createElement('button');
		buttonPrint.classList.add('btn', 'btn-secondary');
		let iPrint = document.createElement('i');
		iPrint.classList.add('fa-solid', 'fa-print');
		buttonPrint.append(iPrint);
		buttonPrint.onclick = async (event) => {

			//Actualizar pedido
			let orderId = event.target.parentNode.id.split('_')[1];
			let order = await updateStatusById(orderId, 'ENVIADO');

			// Eliminar la fila de la tabla
			tr.remove();
			//Generar PDF
			await generateShippingLabel(order);

			await refresh();

		};
		tdPrint.append(buttonPrint);
		// Annadir a la fila
		tr.append(tdPrint);

		tbody.append(tr);

		// Columna eliminar
		let tdDelete = document.createElement('td');
		tdDelete.classList.add('align-middle');
		tdDelete.id = `tdDelete_${order.orderId}`;
		let button = document.createElement('button');
		button.classList.add('btn', 'btn-danger');
		let iDelete = document.createElement('i');
		iDelete.classList.add('fa-solid', 'fa-ban');
		button.append(iDelete);
		button.onclick = (event) => {
			let orderId = event.target.parentNode.id.split('_')[1];
			//Mensaje de confirmacion
			document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres cancelar el pedido?";
			document.getElementById("modalAdminBtnOk").onclick = async () => confirmCancelOrder(tr, orderId);
			$('#adminModal').modal('show');
		};

		tdDelete.append(button);
		// Annadir a la fila
		tr.append(tdDelete);

		tbody.append(tr);
	}
}

// Comprobar filas de la tabla
function verifyEmptyTable() {
	let ordersTable = document.getElementById('ordersTable');
	let tbody = ordersTable.querySelector('tbody');
	let tr = tbody.querySelectorAll('tr');

	if (tr.length === 0) {
		document.getElementById("ordersFound").classList.add('d-none');
		document.getElementById("msgNotFound").classList.remove('d-none');
	}
}

// Cancelar pedido
async function confirmCancelOrder(tr, orderId) {
	//Actualizar pedido
	await updateStatusById(orderId, 'CANCELAR');

	// Eliminar la fila de la tabla
	tr.remove();

	await refresh();
}

// Actualizar elementos vista
async function refresh() {
	await loadAlerts();
	verifyEmptyTable();
}

//Generar PDF
async function generateShippingLabel(order) {

	try {
		let response = await fetch("/orders/generateShippingLabel?orderId=" + BigInt(order.orderId), {
			method: "GET"
		});

		if (response.ok) {
			let blob = await response.blob();
			let url = window.URL.createObjectURL(blob);

			let a = document.createElement("a");
			a.href = url;
			a.download = `Pedido-${order.orderNumber}.pdf`;
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
			window.URL.revokeObjectURL(url);
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}


// Obtener pedidos en estado CREADO
async function searchByCreado() {
	try {
		let response = await fetch("/orders/searchByCreado", {
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

// Obtener actualizar estado de pedido a ENVIADO
async function updateStatusById(orderId, status) {
	try {
		let response;
		if (status === 'ENVIADO') {
			response = await fetch("/orders/updateStatusEnviado?orderId=" + BigInt(orderId), {
				method: "GET"
			});
		} else {
			response = await fetch("/orders/updateStatusCancelado?orderId=" + BigInt(orderId), {
				method: "GET"
			});
		}


		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.order;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}