// Cargar denuncias a revisar
async function loadComplaints() {
	let reports = await searchByWithoutProcessing();
	let reportsFound = document.getElementById("complaintsFound");
	let msgNotFound = document.getElementById("msgNotFound");

	if (reports && reports.length > 0) {
		if (reportsFound) {
			reportsFound.classList.remove('d-none');
		}

		// Maquetar tabla
		await layoutTableReports(reports);
	} else if (msgNotFound) {
		msgNotFound.classList.remove('d-none');
	}
}

// Maquetar tabla de reportes
async function layoutTableReports(reports) {
	let tbody = document.getElementById('tbodyComplaints');

	// Limpiar el contenido del tbody
	tbody.innerHTML = '';

	for (let report of reports) {

		let tr = document.createElement('tr');
		tr.id = `reportRow_${report.reportId}`;
		tr.classList.add('data-row');

		// Columna usuario denunciante
		let tdComplainant = document.createElement('td');
		tdComplainant.classList.add('align-middle');
		tdComplainant.innerHTML = Number(`${report.reporterDto.name} ${report.reporterDto.surname}<br>${report.reporterDto.email}`);
		// Annadir a la fila
		tr.append(tdComplainant);

		// Columna denuncias
		let tdReport = document.createElement('td');
		tdReport.classList.add('align-middle');
		tdReport.textContent = await countByReporter(report.reporterDto);
		// Annadir a la fila
		tr.append(tdReport);

		// Columna Bloquear/Eliminar denuncia
		let tdDeleteReport = document.createElement('td');
		tdDeleteReport.classList.add('align-middle');
		let buttonDeleteReport = document.createElement('button');
		buttonDeleteReport.classList.add('btn', 'btn-danger');
		let iDeleteReport = document.createElement('i');
		iDeleteReport.classList.add('fa-solid', 'fa-ban');
		buttonDeleteReport.append(iDeleteReport);
		buttonDeleteReport.onclick = async () => {

			//Mensaje de confirmacion
			document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que desea eliminar la denuncia y bloquear al usuario denunciante?";
			document.getElementById("modalAdminBtnOk").onclick = async () => confirmDeleteReport(tr, report);
			$('#adminModal').modal('show');

arreglar el metodo refrescar
			await refresh();

		};
		tdPrint.append(buttonPrint);
		// Annadir a la fila
		tr.append(tdPrint);

		tbody.append(tr);



		// Columna Precio total
		let tdTotalPrice = document.createElement('td');
		tdTotalPrice.classList.add('align-middle');
		tdTotalPrice.textContent = order.total.toFixed(2) + '€';
		// Annadir a la fila
		tr.append(tdTotalPrice);



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

// Eliminar reporte y bloquear denunciante
async function confirmCancelOrder(tr, report) {
	//Actualizar pedido
	actualizar reporte a procesado y bloquear denunciante
	await updateStatusById(orderId, 'CANCELAR');

	// Eliminar la fila de la tabla
	tr.remove();

	await refresh();
}

// Contar cantidad de reportes por usuario
async function countByReporter(userDto) {
	try {
		let response = await fetch("/report/countByReporter", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(userDto)
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = "/internalError";
		}

		return data;

	} catch (error) {
		console.error(error);
		window.location.href = "/internalError";
	}
}


// Obtener reportes sin procesar
async function searchByWithoutProcessing() {
	try {
		let response = await fetch("/report/searchByWithoutProcessing", {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.reports;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}