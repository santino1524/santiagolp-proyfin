// Cargar nuevos pedidos
async function loadNewOrders() {
	let orders = await searchByCreado();
	
	// Maquetar tabla
	layoutTableOrders(orders);
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
		tr.append(tdOrder);

		// Columna Precio total
		let tdTotalPrice = document.createElement('td');
		tdTotalPrice.classList.add('align-middle');
		tdTotalPrice.textContent = order.total;
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
		button.append(iPrint);
		button.onclick = (event) => {
			// Eliminar la fila de la tabla
			tr.remove();
			//Actualizar pedido
			let orderId = event.target.parentNode.id.split('_')[1];
			let order = updateStatusById(orderId, 'ENVIADO');

			//Generar PDF
			printLabel(order);
		};
		buttonPrint.append(button);
		// Annadir a la fila
		tr.append(buttonPrint);

		tbody.append(tr);

		// Columna eliminar
		let tdDelete = document.createElement('td');
		tdDelete.classList.add('align-middle');
		tdDelete.id = `tdDelete_${productCar.productId}`;
		let button = document.createElement('button');
		button.classList.add('btn', 'btn-danger');
		let iDelete = document.createElement('i');
		iDelete.classList.add('fa-solid', 'fa-ban');
		button.append(iDelete);
		button.onclick = (event) => {
			let orderId = event.target.parentNode.id.split('_')[1];
			//Mensaje de confirmacion
			document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres cancelar el pedido?";
			document.getElementById("modalAdminBtnOk").onclick = () => confirmCancelOrder(tr, orderId);
			$('#adminModal').modal('show');
		}
	};
	tdDelete.append(button);
	// Annadir a la fila
	tr.append(tdDelete);

	tbody.append(tr);
}

// Cancelar pedido
function confirmCancelOrder(tr, orderId) {
	// Eliminar la fila de la tabla
	tr.remove();

	//Actualizar pedido
	updateStatusById(orderId, 'CANCELAR');
}

//Generar PDF
function printLabel(order) {

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
			response = await fetch("/orders/updateStatusEnviado?orderId=" + orderId, {
				method: "GET"
			});
		} else {
			response = await fetch("/orders/updateStatusCancelado?orderId=" + orderId, {
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