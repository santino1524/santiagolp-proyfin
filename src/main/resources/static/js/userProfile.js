// Cargar pagina 
async function loadUserProfilePage() {
	// Obtener id de usuario autenticado
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);

	// Maquetar tarjetas
	layoutPersonalInformation(user);
	layoutEmail(user);
	layoutAddressesProfile(user);
}

// Maquetar datos personales
function layoutPersonalInformation(user) {
	document.getElementById("userName").append(user.name);
	document.getElementById("surnames").append(user.surname + ' ' + user.secondSurname);
	document.getElementById("userDni").append(user.dni);
	document.getElementById("phone").append(user.phoneNumber);

}

// Maquetar datos personales
function layoutEmail(user) {
	document.getElementById("email").append(user.email);
}

// Maquetar direcciones
async function layoutAddressesProfile(user) {
	let addressesList = document.getElementById("addressesList");
	document.getElementById("buttonAdd").textContent = 'Añadir';

	addressesList.innerHTML = '';

	if (user.addressesDto === null || user.addressesDto.length === 0) {
		addressesList.append('No hay direcciones registradas');
		return;
	}

	for (const address of user.addressesDto) {

		let aAddress = document.createElement('a');
		aAddress.classList.add('list-group-item', 'list-group-item-action', 'item-address');
		aAddress.id = `addressRow_${address.addressId}`;
		aAddress.onclick = (event) => {
			let clickedItem = event.target;
			let listItems = document.querySelectorAll('.list-group-item.item-address');

			// Desactivar todos los elementos del grupo
			listItems.forEach(otherItem => {
				otherItem.classList.remove('active');
			});

			// Activar el elemento en el que se hizo clic
			clickedItem.classList.add('active');

			// Modificar texto dl button
			document.getElementById("buttonAdd").textContent = 'Modificar';
		};
		aAddress.append(address.directionLine + ', ' + address.city + ', ' + address.province + ', C.P: ' + address.cp + ', ' + address.country);
		aAddress.style.fontSize = '1.3em';
		// Icono de eliminar
		let button = document.createElement('button');
		button.classList.add('ml-5', 'btn', 'btn-danger');
		button.id = `addressDelete_${address.addressId}`;
		button.onclick = (event) => confirmDeleteAddress(event.target.parentNode.id.split('_')[1]);
		let deleteIcon = document.createElement('i');
		deleteIcon.classList.add('fas', 'fa-trash', 'delete-icon');
		deleteIcon.style.cursor = 'pointer';
		button.append(deleteIcon);

		aAddress.append(button);
		addressesList.append(aAddress);

	}

}

// Eliminar producto
function confirmDeleteAddress(addressId) {
	//Mensaje de confirmacion
	document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres eliminar el producto?";
	document.getElementById("modalAdminBtnOk").onclick = () => deleteAddress(addressId);
	$('#adminModal').modal('show');
}

// Guardar direccion
async function deleteAddress(addressId) {
	try {
		let response = await fetch("/addresses/delete?addressId=" + parseInt(addressId), {
			method: "DELETE",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			}
		});

		if (response.status !== 200) {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}

	// Actualizar direcciones
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await searchByEmail(email);
	layoutAddressesProfile(user);
}

// Comprobacion de emails
function checkEmails() {
	let newEmail = document.getElementById("newEmail").value;
	let confirmEmail = document.getElementById("confirmEmail").value;
	if (newEmail && confirmEmail && newEmail !== confirmEmail) {
		showMessage(document.getElementById("messageEmailError"), "Las direcciones de correo electrónico no coinciden. Por favor, revisa los datos.");
		return false;
	}

	return true;
}

// Formulario cambiar email
async function saveAddressForm() {
	let formAddress = document.getElementById("emailChange");
	let newEmail = document.getElementById("newEmail").value;
	let confirmEmail = document.getElementById("confirmEmail").value;
	let divMessageAddressError = document.getElementById("messageEmailError");
	let email = document.getElementById("authenticatedUser").textContent;

	if (newEmail && confirmEmail) {

		// Validar los valores
		if (!checkEmails()) {
			return;
		}

		let user = await searchByEmailProfile(email);

		user.email = newEmail;

		user = await updateUser(user);

		if (user) {
			layoutEmail(user);
		}

		formAddress.reset();

		// Cierra el modal
		$('#modalAddress').modal('hide');
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageAddressError, "Completa todos los campos del formulario");
	}
}

// Obtener usuario por email 
async function searchByEmailProfile(email) {
	try {
		let response = await fetch("/users/searchByEmail?email=" + encodeURIComponent(email), {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.user;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Peticion para actualizar producto
async function updateUser(user) {
	let divMessageProductError = document.getElementById("messageUserError");

	try {
		let response = await fetch("/users/update", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify(user)
		});

		if (response.status === 200) {
			return await response.json();
		} else if (response.status === 422) {
			showMessage(divMessageProductError, "Ya existe una cuenta de usuario con el correo electrónico " + user.email);
			return;
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}


