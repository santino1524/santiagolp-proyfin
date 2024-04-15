// Variables globales
let flagSidebar = true;
const regexOnlyWord = /^[a-zA-ZÀ-ÖØ-öø-ÿ]*$/;
/*
// Deshabilitar administracion en dispositivo movil
document.addEventListener('DOMContentLoaded', function() {
	const adminLink = document.getElementById('adminLink');
	if (adminLink) {
		adminLink.addEventListener('click', function(event) {

			// Obtener el ancho de la ventana
			const windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;

			// Definir el ancho maximo para considerar como dispositivo movil
			const mobileWidth = 768;

			// Redireccionar si el ancho de la ventana es menor o igual al ancho definido para dispositivos moviles
			if (windowWidth <= mobileWidth) {
				event.preventDefault();
				flagSidebar = false;
				window.location.href = "/responsiveAdmin";
			}

		});
	}
});

// EStablecer altura del sidebar del panel de administracion
document.addEventListener("DOMContentLoaded", function() {

	// Obtener la altura del footer
	const footHeight = document.getElementById('footer').clientHeight;
	// Obtener la altura del header
	const headHeight = document.getElementById('header').clientHeight;
	// Obtener sidebar
	const sidebar = document.getElementById('sidebar');
	if (sidebar) {
		sidebar.style.height = `calc(100vh - ${footHeight}px - ${headHeight}px)`;
	}
});
*/
// Comprobacion de contrasennas al enviar formulario 
document.addEventListener("DOMContentLoaded", function() {
	let submitButton = document.getElementById("submitButtonRegister");
	if (submitButton) {
		submitButton.addEventListener("click", function(event) {
			if (!checkPasswords()) {
				event.preventDefault();
			}
		});
	}
});

// Comprobacion de contrasennas
function checkPasswords() {
	let password = document.getElementById("passwd").value;
	let confirmPassword = document.getElementById("confirmPasswd").value;
	if (password && confirmPassword && password !== confirmPassword) {
		document.getElementById("passwordError").classList.remove("d-none");
		return false;
	}
	document.getElementById("passwordError").classList.add("d-none");

	return true;
}

// Ativar enlaces navlink
document.addEventListener("DOMContentLoaded", function() {
	const navLinks = document.querySelectorAll(".nav-link");
	navLinks.forEach(function(navLink) {
		navLink.addEventListener("click", function() {
			navLinks.forEach(function(link) {
				link.parentNode.classList.remove("active");
			});
			this.parentNode.classList.add("active");
		});
	});
});

// Genera codigo EAN-13
function generateEAN13() {
	let inputNumber = document.getElementById("productNumber");
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
	inputNumber.value = cuerpoEAN + digitoControl;
}

// Guardar categorias	
let form = document.getElementById("formCategory");
let divMessageCategory = document.getElementById('messageCategory');
let divMessageCategoryError = document.getElementById('messageCategoryError');
let categoryName;
function saveCategory() {
	form = document.getElementById("formCategory");
	divMessageCategory = document.getElementById('messageCategory');
	divMessageCategoryError = document.getElementById('messageCategoryError');

	// Obtener el nombre de la categoria desde el campo de entrada
	categoryName = document.getElementById("categoryName").value;

	if (regexOnlyWord.test(categoryName)) {
		fetch("category/save", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify({
				categoryName: categoryName,
			})
		}).then(response => {
			if (response.status === 204) {
				divMessageCategoryError.classList.add("d-none");
				divMessageCategory.innerText = "La categoría se ha creado";
				divMessageCategory.classList.remove("d-none");
			} else if (response.status === 422) {
				divMessageCategory.classList.add("d-none");
				divMessageCategoryError.innerText = "Ya está registrada una categoría con ese nombre";
				divMessageCategoryError.classList.remove("d-none");
			} else {
				window.location.href = "/internalError";
			}
		}).catch(() => window.location.href = "/internalError");

		form.reset();

		// Mostrar boton de eliminar categoria
		showDeleteButton();
	} else {
		divMessageCategory.classList.add("d-none");
		divMessageCategoryError.innerText = "El nombre solo pueden contener letras";
		divMessageCategoryError.classList.remove("d-none");
	}

	// Actualizar pagina
	window.location.reload(true);
}

// Comprobar si existe Categoria a eliminar
function deleteCategory() {
	form = document.getElementById("formCategory");
	divMessageCategory = document.getElementById('messageCategory');
	divMessageCategoryError = document.getElementById('messageCategoryError');

	// Obtener el nombre de la categoria desde el campo de entrada
	categoryName = document.getElementById("categoryName").value;

	// Comprobar si el valor coincide con el patron
	if (regexOnlyWord.test(categoryName)) {
		fetch("category/searchByName", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify({
				categoryName: categoryName,
			})
		}).then(response => {
			if (response.ok) {
				// La solicitud se completo con exito
				return response.json(); // Devuelve una promesa que se resuelve con el cuerpo de la respuesta como JSON
			} else {
				window.location.href = "/internalError"
			}
		}).then(responseData => {
			// Manipular los datos de la respuesta
			if (responseData.categoryId !== null) {
				// Si se encontro la categoria, realizar la eliminacion
				deleteCategoryById(responseData.categoryId);
			} else {
				divMessageCategory.classList.add("d-none");
				divMessageCategoryError.innerText = "No se ha encontrado ninguna categoría con ese nombre";
				divMessageCategoryError.classList.remove("d-none");
			}
		}).catch(() => window.location.href = "/internalError");

		form.reset();

		// Mostrar boton de eliminar categoria
		showDeleteButton();
	} else {
		divMessageCategory.classList.add("d-none");
		divMessageCategoryError.innerText = "El nombre solo pueden contener letras";
		divMessageCategoryError.classList.remove("d-none");
	}

	// Actualizar pagina
	window.location.reload();
}

// Eliminar categoria por ID
function deleteCategoryById(categoryId) {
	divMessageCategory = document.getElementById('messageCategory');
	divMessageCategoryError = document.getElementById('messageCategoryError');

	fetch("category/delete", {
		method: "DELETE",
		headers: {
			"Content-type": "application/json; charset=utf-8"
		},
		body: JSON.stringify({
			categoryId: categoryId
		})
	}).then(response => {
		if (response.status === 204) {
			divMessageCategoryError.classList.add("d-none");
			divMessageCategory.innerText = "Se ha eliminado correctamente la categoría";
			divMessageCategory.classList.remove("d-none");
		} else {
			window.location.href = "/internalError";
		}
	}).catch(() => window.location.href = "/internalError");
}

// Mostrar boton de eliminar categoria
function showDeleteButton() {
	const inputCategoryName = document.getElementById('categoryName');
	const buttonDeleteCategory = document.getElementById('buttonDeleteCategory');

	if (inputCategoryName.value) {
		buttonDeleteCategory.classList.remove("d-none");
	} else {
		buttonDeleteCategory.classList.add("d-none");
	}
}

// Mostrar todas las categorias
function showCategories() {
	selectCategories = document.getElementById('productCategory');

	fetch("category/searchAll", {
		method: "GET"
	})
		.then(response => {
			if (response.ok) {
				return response.json();
			} else {
				window.location.href = "/internalError"
			}
		})
		.then(data => {
			// Limpiar opciones existentes en el select
			selectCategories.innerHTML = "";

			// Agregar la opcion por defecto
			const defaultOption = document.createElement("option");
			defaultOption.value = "";
			defaultOption.text = "Seleccione";
			selectCategories.appendChild(defaultOption);

			// Iterar sobre los datos y agregar opciones al select
			if (data) {
				data.productCategoryDto.forEach(category => {
					const option = document.createElement("option");
					option.value = category.categoryId;
					option.text = category.categoryName;
					selectCategories.appendChild(option);
				});
			}
		})
		.catch(() => window.location.href = "/internalError");
}

// Enviar formulario Producto
let divMessageProduct = document.getElementById('messageProduct');
let divMessageProductError = document.getElementById('messageProductError');
function submitFormProduct(event) {
	divMessageProduct = document.getElementById('messageProduct');
	divMessageProductError = document.getElementById('messageProductError');
	const form = document.getElementById('formProduct');
	const inputFiles = document.getElementById('files');
	const inputUrlsHidden = document.getElementById('imageUrls');
	const formData = new FormData();

	// Prevenir el envio del formulario
	event.preventDefault();

	// Agregar los archivos seleccionados al objeto FormData
	for (const file of inputFiles.files) {
		formData.append('files', file);
	}

	fetch("products/upload", {
		method: "POST",
		headers: {
			"Content-type": "application/json; charset=utf-8"
		},
		body: files
	}).then(response => {
		if (response.ok) {
			// La solicitud se completo con exito
			return response.json(); // Devuelve una promesa que se resuelve con el cuerpo de la respuesta como JSON
		} else {
			window.location.href = "/internalError"
		}
	}).then(responseData => {
		// Manipular los datos de la respuesta
		if (responseData.imageUrls !== null && responseData.imageUrls.length > 0) {
			inputUrlsHidden.value = responseData.imageUrls.join(',');
		} else {
			window.location.href = "/internalError";
		}
	}).catch(() => window.location.href = "/internalError");

	// Enviar el formulario
	form.submit();
}


//		if (response.status === 204) {
//			divMessageProductError.classList.add("d-none");
//			divMessageProduct.innerText = "El producto ha sido registrado";
//			divMessageProduct.classList.remove("d-none");
//		} else if (response.status === 422) {
//			divMessageCategory.classList.add("d-none");
//			divMessageProduct.innerText = "El número de producto introducido ya está registrado";
//			divMessageProduct.classList.remove("d-none");
//		} else {
//			window.location.href = "/internalError";
//		}