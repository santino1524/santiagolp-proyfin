
//Expresiones regulares
const regexOnlyWord = /^[a-zA-ZÀ-ÖØ-öø-ÿ]*$/;
const regexOnlyWordSpaces = /^[a-zA-ZÀ-ÖØ-öø-ÿ\s]*$/;
const postalCodeRegExp = /^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/;
const onlyWordsNumbersSpaces = /^[a-zA-ZÀ-ÖØ-öø-ÿ\d\s.,;:]*$/;
const passwdRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!*])(?=\S+$).{7,}$/;
const emailRegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const dniRegExp = /^\d{8}[a-zA-Z]$/;
const ivaRegex = /^\d{1,2}$/;
const phoneRegex = /^\d{9}$/;
const basePriceRegex = /^(?:[1-9]\d*|0)?(?:\.\d+)?$/;
const numberProductRegex = /^\d{13}$/;
const separatorsRegex = /[\\/]/;
// Lista de extensiones permitidas
const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
// Url error
const urlError = "/internalError";
// Tamanyo maximo permitido para la carga de imagenes (500KB)
const maxSize = '500KB';
const maxSizeInBytes = 500 * 1024;

// Comprobacion de contrasennas al enviar formulario de registro
document.addEventListener("DOMContentLoaded", function() {
	let submitButton = document.getElementById("submitButtonRegister");
	if (submitButton) {
		submitButton.addEventListener("click", function(event) {
			let password = document.getElementById("passwd").value;
			let confirmPassword = document.getElementById("confirmPasswd").value;
			if (!checkPasswords(password, confirmPassword, document.getElementById("passwordError"))) {
				event.preventDefault();
			}
		});
	}
});


// Limitar entrada en input cantidad de productos
function limitInput(element) {
	if (element.value.length > 2) {
		element.value = element.value.slice(0, 2);
	}
}

// Comprobacion de contrasennas
function checkPasswords(password, confirmPassword, divAlert) {

	if (password && confirmPassword && password !== confirmPassword) {
		showMessage(divAlert, "Las contraseñas no coinciden");
		return false;
	}

	return true;
}

// Activar enlaces navlink
//document.addEventListener("DOMContentLoaded", function() {
//	const navLinks = document.querySelectorAll(".nav-link");
//	navLinks.forEach(function(navLink) {
//		navLink.addEventListener("click", function() {
//			navLinks.forEach(function(link) {
//				link.parentNode.classList.remove("active");
//			});
//			this.parentNode.classList.add("active");
//		});
//	});
//});
//document.addEventListener("DOMContentLoaded", function() {
//    const navLinks = document.querySelectorAll(".nav-link");
//
//    navLinks.forEach(function(navLink) {
//        navLink.addEventListener("click", function(event) {
//            // Evitar que se siga el enlace
//            event.preventDefault();
//
//            // Quitar la clase "active" de todos los enlaces
//            navLinks.forEach(function(link) {
//                link.parentNode.classList.remove("active");
//            });
//
//            // Agregar la clase "active" al enlace clickeado
//            this.parentNode.classList.add("active");
//            
//            
//        });
//    });
//});

// Mostrar alert
function showMessage(div, message) {
	div.innerText = message;
	div.classList.remove('d-none'); // Mostrar el elemento
	document.body.scrollIntoView(true);

	// Ocultar el mensaje despues de 10 segundos
	setTimeout(function() {
		div.classList.add('d-none'); // Ocultar el elemento nuevamente
	}, 10000);
}

// Autenticacion de usuarios en el login pagina
function submitLoginPage(event) {
	let user = document.getElementById('inputEmail').value;
	let password = document.getElementById('inputPassword').value;

	if (verifyUserPass(user, password)) {
		event.preventDefault();
		$('#loginPageModal').modal('show');
	}
}

// Autenticacion de usuarios en el login nav
function submitLoginNav(event) {
	let user = document.getElementById('inputEmail2').value;
	let password = document.getElementById('inputPassword2').value;

	if (verifyUserPass(user, password)) {
		event.preventDefault();
		$('#loginModal').modal('show');
	}
}

// Verificar usuario y passwd
function verifyUserPass(user, password) {
	return user === "goku@mail.com" && password === "goku";
}

// Listar categorias en nav
async function listCategoriesInNav() {
	let dropdownMenu = document.getElementById('categoriesMenu');

	try {
		let response = await fetch("/category/searchAll", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.productCategoryDto.length > 0) {
			// Limpiar el contenido actual del dropdownMenu
			dropdownMenu.innerHTML = '';

			// Crear menu de categorias dinamicamente
			data.productCategoryDto.forEach(category => {
				let link = document.createElement('a');
				link.classList.add('dropdown-item', 'bg-dark', 'text-white');
				link.href = "/products/searchByCategoryPageProducts/" + category.categoryId;
				link.textContent = category.categoryName;

				dropdownMenu.appendChild(link);
			});
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Llamar a la funcion para actualizar el numero de productos en el carrito cuando se cargue la pagina
document.addEventListener('DOMContentLoaded', function() {
	addCart();
});

// Actualizar cantidad en cesta
function updateQuantityCart(quantity) {
	// Actualizar el contenido del número de productos en la cesta
	document.getElementById('cartQuantity').innerText = quantity;
}

// Carga de annadir al carrito
function loadingAddCart() {
	let button = document.getElementById("addToCartButton");

	if (button) {
		button.classList.add("loading");

		// Simular una operacion de carga
		setTimeout(function() {

			button.classList.remove("loading");
		}, 1500);
	}
}

// Annadir al carrito
function addCart(productId, quantity) {
	if (productId) {
		productId = productId.toString();
	}
	// Efecto de carga
	loadingAddCart();

	// Obtener el carrito de la cesta del localStorage
	let cartLfd = JSON.parse(localStorage.getItem('cartLfd')) || [];

	// Verificar si el producto ya esta en el carrito
	let existingProduct = cartLfd.find(product => product.productId == productId);
	let sumProductsCar;

	if (existingProduct) {
		// Si el producto ya existe en el carrito, incrementar la cantidad
		sumProductsCar = parseInt(existingProduct.quantity) + parseInt(quantity || 1);
		if (sumProductsCar <= 99) {
			existingProduct.quantity = sumProductsCar;

			// Actualizar cantidad en carrito y guardar productos en el
			operationCart(cartLfd);
		}

	} else {
		// Si el producto no existe, agregarlo al carrito
		let product;
		if (productId && (quantity || 1)) {
			product = {
				productId: productId,
				quantity: parseInt(quantity) || 1,
			};
			cartLfd.push(product);
		}

		// Actualizar cantidad en carrito y guardar productos en el
		operationCart(cartLfd);
	}


}

// Actualizar cantidad en carrito y guardar productos en el
function operationCart(cartLfd) {
	// Contar el numero total de productos en el carrito
	let totalProducts = cartLfd.reduce((total, productCart) => total + parseInt(productCart.quantity), 0);

	// Actualizar icono carrito
	updateQuantityCart(totalProducts);

	// Guardar el carrito actualizado en el localStorage
	localStorage.setItem('cartLfd', JSON.stringify(cartLfd));
}

// Al cerrar el modal si esta en el carrito que lo refresque
document.addEventListener('DOMContentLoaded', function() {
	let modalProductClose = document.getElementById("modalProductClose");
	if (modalProductClose) {
		modalProductClose.onclick = function() {
			// Verificar si la ruta de la pagina actual contiene "shoppingCart"
			if (window.location.href.includes("shoppingCart")) {
				window.location.href = "/shoppingCart";
			}
		};
	}
});

// Buscar productos y redirigir a pagina productos
async function searchByCategoryPageProducts(categoryId) {
	try {
		await fetch("/products/searchByCategoryPageProducts/" + categoryId, {
			method: "GET",
		});

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Mostrar producto en modal 
async function showModalProduct(product) {
	$('#modalProduct').modal('show');

	// Limpiar el contenido anterior antes de agregar nuevo contenido
	document.getElementById('carouselInner').innerHTML = '';
	document.getElementById('carouselIndicators').innerHTML = '';
	document.getElementById('messageProductQuantity').classList.add('d-none');

	document.getElementById('titleModalProduct').textContent = await searchCategoryNameById(product.categoryId);

	// Añadir imagenes
	//Primera imagen
	let divCarouselItemActive = document.createElement('div');
	divCarouselItemActive.classList.add('carousel-item', 'active');
	let imgActive = document.createElement('img');
	imgActive.classList.add('d-block', 'w-100');
	imgActive.src = 'data:image/jpeg;base64,' + product.images[0];
	divCarouselItemActive.append(imgActive);
	document.getElementById('carouselInner').append(divCarouselItemActive);

	// Primera Slide
	let liSlide1 = document.createElement('li');
	liSlide1.setAttribute('data-target', '#carouselImagesProducts');
	liSlide1.setAttribute('data-slide-to', '0');
	liSlide1.classList.add('active');
	document.getElementById('carouselIndicators').append(liSlide1);

	// Otras imagenes
	for (let i = 1; i < product.images.length; i++) {
		let divCarouselItem = document.createElement('div');
		divCarouselItem.classList.add('carousel-item');
		let img = document.createElement('img');
		img.classList.add('d-block', 'w-100');
		img.src = 'data:image/jpeg;base64,' + product.images[i];
		divCarouselItem.append(img);
		document.getElementById('carouselInner').append(divCarouselItem);

		// Crear Slide
		let liSlide = document.createElement('li');
		liSlide.setAttribute('data-target', '#carouselImagesProducts');
		liSlide.setAttribute('data-slide-to', i);
		document.getElementById('carouselIndicators').append(liSlide);
	}

	// Detalles del producto
	document.getElementById('productId').value = product.productId;
	document.getElementById('productName').textContent = product.productName;
	document.getElementById('productPrice').textContent = product.pvpPrice.toFixed(2) + "€";
	document.getElementById('productSize').textContent = "Tamaño: " + product.productSize;
	document.getElementById('productDescription').textContent = product.productDescription;

	if (product.productQuantity === 0) {
		document.getElementById('messageProductQuantity').innerText = "Este producto está temporalmente agotado";
		document.getElementById('messageProductQuantity').classList.remove('d-none');
	} else if (product.productQuantity < 5) {
		document.getElementById('messageProductQuantity').innerText = "Solo quedan " + product.productQuantity + " productos en stock";
		document.getElementById('messageProductQuantity').classList.remove('d-none');
	}
}

// Buscar categoria por ID
async function searchCategoryNameById(categoryId) {
	try {
		let response = await fetch("/category/searchById?categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.category.categoryId) {
			return data.category.categoryName;
		} else {
			window.location.href = urlError;
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Mostrar modal Address
async function addAddress() {
	$('#modalAddress').modal('show');
}

// Formulario guardar nueva direccion
async function saveAddressForm() {
	let formAddress = document.getElementById("formAddress");
	let addressId = document.getElementById("addressId").value;
	let addressLine = document.getElementById("addressLine").value;
	let city = document.getElementById("city").value;
	let province = document.getElementById("province").value;
	let postalCode = document.getElementById("postalCode").value;
	let divMessageAddressError = document.getElementById("messageAddressError");
	let email = document.getElementById("authenticatedUser").textContent;
	let formData = new FormData();

	if (addressLine && city && province && postalCode) {

		// Validar los valores usando el patron
		if (!onlyWordsNumbersSpaces.test(addressLine)) {
			showMessage(divMessageAddressError, "Para la dirección solo se permiten palabras, números y espacios");

			return;
		}

		if (!regexOnlyWordSpaces.test(city)) {
			showMessage(divMessageAddressError, "Para la ciudad solo se permiten palabras con espacios");

			return;
		}

		if (!regexOnlyWordSpaces.test(province)) {
			showMessage(divMessageAddressError, "Para la provincia solo se permiten palabras con espacios");

			return;
		}

		if (!postalCodeRegExp.test(postalCode)) {
			showMessage(divMessageAddressError, "El código postal introducido es incorrecto");

			return;
		}

		let user = await searchByEmail(email);

		if (addressId) {
			formData.append('addressId', addressId);
		}
		formData.append('cp', postalCode);
		formData.append('directionLine', addressLine.trim());
		formData.append('city', city.trim());
		formData.append('province', province.trim());
		formData.append('country', "España");
		formData.append('userId', user.userId);

		let newAddress = await saveAddress(formData);

		// Maquetar direccion
		if (document.getElementById("addressesList")) {
			layoutAddressesProfile(await searchByEmail(user.email));
		} else {
			layoutAddresses(newAddress);
		}

		document.getElementById('addressId').value = "";
		formAddress.reset();

		// Cierra el modal
		$('#modalAddress').modal('hide');
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageAddressError, "Completa todos los campos del formulario");
	}
}

// Guardar direccion
async function saveAddress(formData) {
	let data;

	try {
		let response = await fetch("/addresses/save", {
			method: "POST",
			body: formData
		});

		if (response.status === 200) {
			data = await response.json();
		} else if (response.status === 422) {
			showMessage(divMessageProductError, "El usuario ya tiene ragistrada una dirección con esos datos");
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

// Genera codigo EAN-13
function generateEAN13() {
	let cuerpoEAN = '';

	for (let i = 0; i < 12; i++) {
		cuerpoEAN += Math.floor(Math.random() * 10);
	}

	// Calcula el digito de control (ultimo digito) utilizando el algoritmo EAN-13
	let sumaPares = 0;
	let sumaImpares = 0;
	for (let i = 0; i < cuerpoEAN.length; i++) {
		if (i % 2 === 0) {
			sumaPares += parseInt(cuerpoEAN[i]);
		} else {
			sumaImpares += parseInt(cuerpoEAN[i]);
		}
	}
	let total = (sumaPares * 3) + sumaImpares;
	let digitoControl = (10 - (total % 10)) % 10;

	// Retorna el codigo EAN-13 completo
	return cuerpoEAN + digitoControl;
}

// Buscar producto por ID
async function searchProductById(productId) {
	try {
		let response = await fetch("/products/searchById?productId=" + productId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.product.productId) {
			return data.product;
		} else {
			window.location.href = urlError;
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Obtener usuario por email 
async function searchByEmail(email) {
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

// Obtener todas las direcciones del usuario
async function searchPostalAddressByUser(userId) {
	try {
		let response = await fetch("/addresses/searchByUser?userId=" + userId, {
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

// Obtener la direccion por Id
async function searchPostalAddressById(addressId) {
	try {
		let response = await fetch("/addresses/searchById?addressId=" + addressId, {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data.address;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Cargar notificaciones a gestionar
async function loadAlerts() {
	// Retornar cantidad de pedidos creados
	let newsOrders = await countByStatus();

	// Actualizar el contenido del número de pedidos
	document.getElementById('newsOrders').innerText = newsOrders;

}

// Retornar cantidad de pedidos creados
async function countByStatus() {
	try {
		let response = await fetch("/orders/countByStatus", {
			method: "GET"
		});

		let data;

		if (response.status === 200) {
			data = await response.json();
		} else {
			window.location.href = urlError;
		}

		return data;

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}


// Formatear fecha
function formatDate(orderDate) {
	// Crear un objeto Date a partir de la cadena de fecha y hora
	let dataDate = new Date(orderDate);

	// Obtener los componentes de la fecha
	let day = dataDate.getDate();
	let month = dataDate.getMonth() + 1;
	let year = dataDate.getFullYear();

	// Formatear la fecha
	return `${day < 10 ? '0' + day : day}-${month < 10 ? '0' + month : month}-${year}`;
}

// Abrir pagina de recuperar contrasenna
async function loadRecoverPasswd(email) {
	let divMessagePage = document.getElementById('messageEmailErrorPage');
	let divMessageNav = document.getElementById('messageEmailErrorNav');

	if (!email) {
		// Mostrar mensaje de introducir el email
		let message = "Debe introducir el correo electrónico";
		if (divMessagePage) {
			showMessage(divMessagePage, message);
		}
		if (divMessageNav) {
			showMessage(divMessageNav, message);
		}

		return;
	}

	// Validar email
	if (!emailRegExp.test(email)) {
		let message = "Debe introducir una dirección de correo válida";
		if (divMessageNav) {
			showMessage(divMessageNav, message);
		}
		if (divMessagePage) {
			showMessage(divMessagePage, message);
		}

		return;
	}

	// Obtener id de usuario
	let user = await searchByEmail(email);



	if (user && user.userId) {
		if (user.userId === 1) {
			// El usuario del sistema no contiene preguntas de recuperación
			let message = "El usuario de inicio de la aplicación no contiene preguntas de recuperación";
			if (divMessageNav) {
				showMessage(divMessageNav, message);
			}
			if (divMessagePage) {
				showMessage(divMessagePage, message);
			}

			return;
		}

		// Redireccionar a pagina de recuperacion de contrasennas
		window.location.href = "/users/recoverPassword?userId=" + user.userId;
	} else {
		// No hay usuario registrado con ese email
		let message = "No existe ningún usuario registrado con ese correo electrónico";
		if (divMessageNav) {
			showMessage(divMessageNav, message);
		}
		if (divMessagePage) {
			showMessage(divMessagePage, message);
		}
	}
}

// Cargar nuevos pedidos
async function loadNewOrders() {
	await loadAlerts();
	let orders = await searchByCreado();
	let ordersFound = document.getElementById("ordersFound");
	let msgNotFound = document.getElementById("msgNotFound");

	if (orders && orders.length > 0) {
		if (ordersFound) {
			ordersFound.classList.remove('d-none');
		}

		// Maquetar tabla
		await layoutTableOrders(orders);
	} else if (msgNotFound) {
		msgNotFound.classList.remove('d-none');
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