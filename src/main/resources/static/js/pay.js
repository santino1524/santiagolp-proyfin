// Obtener todas las direcciones
async function showPostalAddress() {
	try {
		let response = await fetch("/addresses/searchAll", {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.addresses;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Obtener todas las direcciones
async function searchPostalAddressById() {
	try {
		let response = await fetch("/addresses/searchById", {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.addresses;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Pagar
function pay() {
	let selectAddress = JSON.parse(localStorage.getItem('selectAddress'));
	
	if (hasItem) {
	console.log('test');
	} else {
		document.getElementById('bodyModalPay').textContent = "Debe seleccionar una dirección de envío antes de proceder al pago";
	}
}

// Cargar pagina de Pago
async function loadPayPage() {
	// Obtener el total a pagar del localStorage
	let moneyToPay = JSON.parse(localStorage.getItem('moneyToPay')) || '0€';
	document.getElementById("moneyToPay").textContent = moneyToPay;
	//Obtener direccion de la ultima compra
	let lastAddress = JSON.parse(localStorage.getItem('lastAddress'));

	let addresses = await showPostalAddress();

	if (addresses === null || addresses.length === 0) {
		let message = "No hay registrada ninguna dirección de envío.";
		showMessage(document.getElementById("messageNotFoundAddresses"), message)

		return;
	} else {
		
	}
	

}