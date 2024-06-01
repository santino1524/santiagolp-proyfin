// Buscar usuario
async function searchUser() {
	hideItems();
	let criterio = document.getElementById('criterio').value;
	let value = document.getElementById('value').value;

	if (!value) {
		showMessage(document.getElementById("messageError"), "Introduce el valor para la búsqueda");

		return false;
	}

	if (criterio === "") {
		showMessage(document.getElementById("messageError"), "Introduce el témino de búsqueda");

		return false;
	}

	// Validar campos segun criterio
	let isValid = false;
	switch (criterio) {
		case "dni":
			isValid = verifyDni(value);
			break;
		case "email":
			isValid = verifyEmail(value);
			break;
		case "numberPhone":
			isValid = verifyPhone(value);
			break;
	}

	if (!isValid) {
		return;
	}

	try {
		let url = `/users/searchByCriterio?criterio=${encodeURIComponent(criterio)}&value=${encodeURIComponent(value)}`;

		let response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			}
		});

		if (!response.ok) {
			window.location.href = urlError;

			return;
		}

		let data = await response.json();
		// Maquetar usuario encontrado
		layoutUser(data.user);


	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Validar DNI
function verifyDni(value) {
	if (!dniRegExp.test(value)) {
		showMessage(document.getElementById("messageError"), "Debe introducir un DNI/NIE válido");

		return;
	}

	return true;
}

// Validar Email
function verifyEmail(value) {
	if (!emailRegExp.test(value)) {
		showMessage(document.getElementById("messageError"), "Debe introducir un Email válido");

		return;
	}

	return true;
}

// Validar Email
function verifyPhone(value) {
	if (!phoneRegex.test(value)) {
		showMessage(document.getElementById("messageError"), "Debe introducir un número de teléfono móvil válido");

		return;
	}

	return true;
}

// ocultar elementos
function hideItems() {
	document.getElementById('adminCards').classList.add('d-none');
	document.getElementById('msgNotFoundUsers').classList.add('d-none');
	document.getElementById('dataUser').classList.add('d-none');
	document.getElementById('passwdVisual').classList.add('d-none');
	document.getElementById('btnChangePasswd').disabled = true;
	document.getElementById('btnRoleChange').disabled = true;
	document.getElementById('userId').value = "";
	document.getElementById('userRole').value = "";
}

// Maquetar usuario encontrado
function layoutUser(user) {
	let adminCards = document.getElementById('adminCards');
	let msgNotFound = document.getElementById('msgNotFoundUsers');
	let dataUser = document.getElementById('dataUser');
	let passwdVisual = document.getElementById('passwdVisual');
	let btnChangePasswd = document.getElementById('btnChangePasswd');
	let btnRoleChange = document.getElementById('btnRoleChange');

	if (!user) {
		hideItems();
		msgNotFound.classList.remove('d-none');
	} else {
		adminCards.classList.remove('d-none');
		msgNotFound.classList.add('d-none');
		dataUser.classList.remove('d-none');

		// Maquetar datos de usuario
		let userName = document.getElementById("userName");
		let surnames = document.getElementById("surnames");
		let phone = document.getElementById("phone");
		let userDni = document.getElementById("userDni");
		let blocked = document.getElementById("blocked");

		userName.textContent = '';
		surnames.textContent = '';
		phone.textContent = '';
		userDni.textContent = '';
		blocked.textContent = '';

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
		if (user.blocked) {
			blocked.append(`${user.name} ${user.surname} no tiene permitido calificar productos comprados, escribir reseñas y/o denunciar comentarios.`);
		}

		// Maquetar role
		let hRole = document.getElementById("role");
		hRole.textContent = '';
		let strong = document.createElement('strong');
		strong.textContent = 'Rol de usuario: ';
		hRole.append(strong);
		hRole.append(user.role === 2 ? 'Comprador' : 'Vendedor');

		document.getElementById('userId').value = user.userId;
		document.getElementById('userRole').value = user.role;
		btnChangePasswd.disabled = false;
		btnRoleChange.disabled = false;
		passwdVisual.classList.remove('d-none');
	}
}

// Guardar rol
async function saveRole() {
	let rol = document.getElementById("roles").value;
	let divMessageRoleError = document.getElementById("messageRoleError");
	let divMessageRole = document.getElementById("messageRole");
	let userId = document.getElementById("userId").value;

	if (rol && userId) {
		if (userId == 1) {
			showMessage(divMessageRoleError, "Al usuario del sistema no se le puede cambiar el rol");

			return;
		}

		let user = await Promise.resolve(searchById(BigInt(userId)));

		user.role = rol;

		// Actualizar role
		user = await updateUser(user);

		if (user && user.userId !== null) {
			showMessage(divMessageRole, "Se ha actualizado el rol de usuario correctamente");
		} else {
			showMessage(divMessageRoleError, "Ha ocurrido un error inesperado");
			return;
		}

		// Maquetar usuario encontrado
		layoutUser(user);
	}



	// Cierra el modal
	$('#roleChange').modal('hide');
}

// Establecer valor select
function setValueSelect() {
	let rol = document.getElementById('userRole').value;
	let roleSelect = document.getElementById('roles');

	if (rol && roleSelect) {
		roleSelect.value = parseInt(rol);
	}
}

// Habilitar/Deshabilitar button role
function actionButtonRole() {
	let rol = document.getElementById('userRole').value;
	let roleSelect = document.getElementById('roles').value;
	let buttonRole = document.getElementById('buttonRole');

	if (rol && roleSelect && parseInt(rol) === roleSelect) {
		buttonRole.disabled = true;
	} else {
		buttonRole.disabled = false;
	}
}

// Guardar contrasenna
async function savePasswd() {
	let newPassword = document.getElementById("newPassword").value;
	let confirmPassword = document.getElementById("confirmPasswd").value;
	let divMessagePasswdError = document.getElementById("messagePasswdError");
	let divMessagePasswd = document.getElementById("messagePasswd");
	let formChangePasswd = document.getElementById("formChangePasswd");
	let userId = document.getElementById("userId").value;

	if (!newPassword || !confirmPassword) {
		// Datos incompletos en el formulario
		showMessage(divMessagePasswdError, "Completa los campos requeridos");
		return;
	}

	// Comprobar que sean iguales las contrasennas
	if (!checkPasswords(newPassword, confirmPassword, divMessagePasswdError)) {
		return;
	}

	// Comprobar complejidad
	if (!passwdRegex.test(newPassword)) {
		showMessage(divMessagePasswdError, "La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un dígito, un carácter especial y tener al menos 7 caracteres");

		return;
	}

	let user = await Promise.resolve(searchById(BigInt(userId)));

	user.passwd = newPassword;

	// Actualizar pass
	user = await updateUser(user);

	if (user && user.userId !== null) {
		showMessage(divMessagePasswd, "La contraseña se ha actualizado correctamente");
	} else {
		showMessage(divMessagePasswdError, "Ha ocurrido un error inesperado");
		return;
	}

	formChangePasswd.reset();

	// Maquetar usuario encontrado
	layoutUser(user);

	// Cierra el modal
	$('#changePasswd').modal('hide');
}

// Peticion para actualizar usuario
async function updateUser(user) {
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
		} else {
			window.location.href = urlError;
		}

		return data.user;
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}