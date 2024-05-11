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
	let userName = document.getElementById("userName");
	let surnames = document.getElementById("surnames");
	let phone = document.getElementById("phone");
	let userDni = document.getElementById("userDni");

	userName.textContent = '';
	surnames.textContent = '';
	phone.textContent = '';
	userDni.textContent = '';

	let strongUserName = document.createElement('strong');
	let strongSurname = document.createElement('strong');
	let strongPhone = document.createElement('strong');
	let strongDni = document.createElement('strong');

	strongUserName.textContent = 'Nombre: ';
	strongSurname.textContent = 'Apellidos: ';
	strongPhone.textContent = 'Teléfono: ';
	strongDni.textContent = 'DNI: ';

	userName.append(strongUserName);
	surnames.append(strongSurname);
	phone.append(strongPhone);
	userDni.append(strongDni);

	userName.append(user.name);
	userDni.append(user.dni);
	surnames.append(user.surname + ' ' + user.secondSurname);
	phone.append(user.phoneNumber);

}

// Cerrar sesion
function logout() {
	window.location.href = "/logout";
}

// Maquetar datos personales
function layoutEmail(user) {
	let hEmail = document.getElementById("email");
	hEmail.textContent = '';
	let strong = document.createElement('strong');
	strong.textContent = 'Email: ';
	hEmail.append(strong);
	hEmail.append(user.email);
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
		aAddress.onclick = () => {
			document.querySelectorAll('.list-group-item.item-address');
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
async function saveEmailForm() {
	let formEmail = document.getElementById("emailChangeForm");
	let newEmail = document.getElementById("newEmail").value;
	let confirmEmail = document.getElementById("confirmEmail").value;
	let divMessageEmailError = document.getElementById("messageEmailError");
	let email = document.getElementById("authenticatedUser").textContent;

	if (newEmail && confirmEmail) {

		// Validar los valores
		if (!emailRegExp.test(newEmail) || !emailRegExp.test(confirmEmail)) {
			showMessage(divMessageEmailError, "Debe introducir una dirección de correo válida");

			return;
		}

		if (!checkEmails()) {
			return;
		}

		let user = await Promise.resolve(searchByEmail(email));

		user.email = newEmail;

		user = await updateUser(user);

		formEmail.reset();

		// Cierra el modal
		$('#emailChange').modal('hide');

		// cerrar sesion
		$('#logoutModal').modal('show');
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageEmailError, "Completa todos los campos del formulario");
	}
}

// Verificar contrasenna actual
async function verifyPasswd(user) {
	try {
		const response = await fetch('/users/verifyOldPasswd', {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify(user)
		});

		if (response.ok) {
			return true;
		} else if (response.status === 422) {
			return false;
		} else {
			window.location.href = urlError;
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Formulario cambiar contrasenna
async function savePasswdForm() {
	let formChangePasswd = document.getElementById("formChangePasswd");
	let oldPassword = document.getElementById("oldPassword").value;
	let newPassword = document.getElementById("newPassword").value;
	let confirmPassword = document.getElementById("confirmPasswd").value;
	let divMessagePasswdError = document.getElementById("messagePasswdError");
	let divMessagePasswd = document.getElementById("messagePasswd");
	let email = document.getElementById("authenticatedUser").textContent;

	if (!oldPassword || !newPassword || !confirmPassword) {
		// Datos incompletas en el formulario
		showMessage(divMessagePasswdError, "Completa todos los campos del formulario");
		return;
	}

	// Comprobar que sean iguales las contrasennas
	if (!checkPasswords(newPassword, confirmPassword, divMessagePasswdError)) {
		return;
	}

	if (!passwdRegex.test(newPassword)) {
		showMessage(divMessagePasswdError, "La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un dígito, un carácter especial y tener al menos 7 caracteres");

		return;
	}

	let user = await Promise.resolve(searchByEmail(email));

	user.passwd = oldPassword;

	// Verificar contrasenna actual
	if (!await verifyPasswd(user)) {
		showMessage(divMessagePasswdError, "La contraseña actual que ha introducido es incorrecta");
		return;
	}

	user.passwd = newPassword;

	// Actualizar
	user = await updateUser(user);

	formChangePasswd.reset();

	// Cierra el modal
	$('#changePasswd').modal('hide');

	showMessage(divMessagePasswd, "La contraseña ha sido actualizada");


}

// Mostrar datos en modal personal informacion
async function showPersonalInformation() {
	let email = document.getElementById("authenticatedUser").textContent;
	let user = await Promise.resolve(searchByEmail(email));

	document.getElementById("phoneInput").value = user.phoneNumber;
	document.getElementById("surNameInput").value = user.surname;
	document.getElementById("secondSurNameInput").value = user.secondSurname;
	document.getElementById("userNameInput").value = user.name;

}

// Formulario cambiar informacion personal
async function savePersonalInformationForm() {
	let formPersonalInformation = document.getElementById("formPersonalInformation");
	let phoneInput = document.getElementById("phoneInput").value;
	let surNameInput = document.getElementById("surNameInput").value;
	let secondSurNameInput = document.getElementById("secondSurNameInput").value;
	let userNameInput = document.getElementById("userNameInput").value;
	let divMessageUser = document.getElementById("messagePersonalInformation");
	let divMessageUserError = document.getElementById("messagePersonalInformationError");
	let email = document.getElementById("authenticatedUser").textContent;

	if (userNameInput && surNameInput && phoneInput && secondSurNameInput) {

		// Validar los valores
		if (!regexOnlyWordSpaces.test(userNameInput)) {
			showMessage(divMessageUserError, "El nombre contiene caracteres no válidos");

			return;
		}
		if (!regexOnlyWordSpaces.test(surNameInput)) {
			showMessage(divMessageUserError, "El primer apellido contiene caracteres no válidos");

			return;
		}
		if (!regexOnlyWordSpaces.test(secondSurNameInput)) {
			showMessage(divMessageUserError, "El segundo apellido contiene caracteres no válidos");

			return;
		}
		if (!phoneRegex.test(phoneInput)) {
			showMessage(divMessageUserError, "Debe introducir un número de teléfono móvil válido, sin el prefijo");

			return;
		}

		let user = await Promise.resolve(searchByEmail(email));

		user.name = userNameInput;
		user.surname = surNameInput;
		user.secondSurname = secondSurNameInput;
		user.phoneNumber = phoneInput;

		user = await updateUser(user);

		formPersonalInformation.reset();

		// Cierra el modal
		$('#personalInformation').modal('hide');

		showMessage(divMessageUser, "Se han actualizado los datos personales");

		if (user) {
			layoutPersonalInformation(user);
		}
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageUserError, "Completa todos los campos del formulario");
	}
}

// Peticion para actualizar producto
async function updateUser(user) {
	let divMessageEmailError = document.getElementById("messageEmailError");
	let divMessagePersonalInformationError = document.getElementById("messagePersonalInformationError");

	let data;

	try {
		let response = await fetch("/users/update", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify(user)
		});

		if (response.status === 200) {
			data = await response.json();
		} else if (response.status === 422) {
			if (divMessageEmailError) {
				showMessage(divMessageEmailError, "Ya existe una cuenta de usuario con el correo introducido");
			}
			if (divMessagePersonalInformationError) {
				showMessage(divMessageEmailError, "Ya existe una cuenta de usuario con el teléfono introducido");
			}
			return;
		} else {
			window.location.href = urlError;
		}

		return data.user;
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}


