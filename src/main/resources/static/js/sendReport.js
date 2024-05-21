// Denunciar comentario
async function reportReview() {
	// Obtener resenna 
	let review = JSON.parse(localStorage.getItem('reviewToReport'));
	let reporter = JSON.parse(localStorage.getItem('reporter'));

	if (review && reporter) {
		review.reported = true;

		// Actualizar review
		review = await saveProductReview(review);

		let report = {
			reviewDto: review,
			reporterDto: reporter
		};

		// Guardar denuncia
		await saveReport(report);

		localStorage.removeItem('reviewToReport');
		localStorage.removeItem('reporter');

		// Cerrar ventana
		window.close()
	}
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

// Guardar resenna
async function saveProductReview(productReviewDto) {
	try {
		let response = await fetch("/productReview/save", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(productReviewDto)
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = "/internalError";
		}

		return data.productReview;

	} catch (error) {
		console.error(error);
		window.location.href = "/internalError";
	}
}