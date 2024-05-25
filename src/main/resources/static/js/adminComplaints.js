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

	// Desactivar loader
	loaderDeactivate();
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
		tdComplainant.innerHTML = `${report.reporterDto.name} ${report.reporterDto.surname}<br>${report.reporterDto.email}`;
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

			await refresh();
		};
		tdDeleteReport.append(buttonDeleteReport);
		// Annadir a la fila
		tr.append(tdDeleteReport);

		// Columna usuario denunciado
		let tdReportedUser = document.createElement('td');
		tdReportedUser.classList.add('align-middle');
		tdReportedUser.innerHTML = `${report.reviewDto.user.name} ${report.reviewDto.user.surname}<br>${report.reviewDto.user.email}`;
		// Annadir a la fila
		tr.append(tdReportedUser);

		// Columna Comentario denunciado
		let tdComment = document.createElement('td');
		tdComment.classList.add('align-middle');
		let aComment = document.createElement('a');
		aComment.href = '#';
		aComment.style.textDecoration = 'none';
		aComment.textContent = 'Ver comentario';
		aComment.onclick = () => {
			// Mostrar Modal con Product
			document.getElementById("bodyModalPay").innerHTML = report.reviewDto.comment;
			document.getElementById("titlePay").innerHTML = 'Comentario del producto';
			$('#modalPay').modal('show');
		};
		tdComment.append(aComment);
		// Annadir a la fila
		tr.append(tdComment);

		// Columna Bloquear/Eliminar comentario
		let tdDeleteComment = document.createElement('td');
		tdDeleteComment.classList.add('align-middle');
		let buttonDeleteComment = document.createElement('button');
		buttonDeleteComment.classList.add('btn', 'btn-danger');
		let iDeleteComment = document.createElement('i');
		iDeleteComment.classList.add('fa-solid', 'fa-ban');
		buttonDeleteComment.append(iDeleteComment);
		buttonDeleteComment.onclick = async () => {

			//Mensaje de confirmacion
			document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que desea eliminar el comentario y bloquear al usuario denunciado?";
			document.getElementById("modalAdminBtnOk").onclick = async () => confirmDeleteComment(tr, report);
			$('#adminModal').modal('show');

			await refresh();
		};
		tdDeleteComment.append(buttonDeleteComment);
		// Annadir a la fila
		tr.append(tdDeleteComment);

		tbody.append(tr);
	}
}

// Eliminar reporte y bloquear denunciante
async function confirmDeleteReport(tr, report) {
	report.reporterDto.blocked = true;

	// Bloquear denunciante
	await updateUser(report.reporterDto);

	//Eliminar reporte
	report.processed = true;
	await saveReport(report);

	// Desmarcar resenna como denunciada
	report.reviewDto.reported = false;
	await saveProductReview(report.reviewDto);

	// Eliminar la fila de la tabla
	tr.remove();

	await refresh();
}

// Guardar denuncia
async function saveReport(reportDto) {
	try {
		let response = await fetch("/report/save", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(reportDto)
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = "/internalError";
		}

		return data.report;

	} catch (error) {
		console.error(error);
		window.location.href = "/internalError";
	}
}

// Eliminar comentario y bloquear denunciado
async function confirmDeleteComment(tr, report) {
	report.reviewDto.user.blocked = true;

	// Bloquear denunciado
	await updateUser(report.reviewDto.user);

	// Eliminar comentario
	report.reviewDto.rating = 0;
	await saveProductReview(report.reviewDto);

	// Eliminar reporte
	report.processed = true;
	await saveReport(report);

	// Eliminar la fila de la tabla
	tr.remove();

	await refresh();
}

// Actualizar elementos vista
async function refresh() {
	await loadAlerts();
	verifyEmptyTable();
}

// Comprobar filas de la tabla
function verifyEmptyTable() {
	let ordersTable = document.getElementById('complaintsTable');
	let tbody = ordersTable.querySelector('tbody');
	let tr = tbody.querySelectorAll('tr');

	if (tr.length === 0) {
		document.getElementById("complaintsFound").classList.add('d-none');
		document.getElementById("msgNotFound").classList.remove('d-none');
	}
}

// Eliminar reporte por ID
async function deleteReport(reportDto) {
	try {
		let response = await fetch("/report/delete", {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(reportDto)
		});

		if (response.status !== 200) {
			window.location.href = "/internalError";
			return;
		}

	} catch (error) {
		console.error(error);
		window.location.href = "/internalError";
	}
}

// Bloquear usuario
async function updateUser(user) {

	try {
		let response = await fetch("/users/update", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify(user)
		});

		if (response.status !== 200) {
			window.location.href = urlError;
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
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