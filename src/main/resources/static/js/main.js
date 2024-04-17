//Expresiones regulares
const regexOnlyWord = /^[a-zA-ZÀ-ÖØ-öø-ÿ]*$/;
const onlyWordsNumbersSpaces = /^[a-zA-ZÀ-ÖØ-öø-ÿ\d\s]*$/;
const ivaRegex = /^\d{1,2}$/;
const basePriceRegex = /^(?:[1-9]\d*|0)?(?:\.\d+)?$/;

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
	categoryName = document.getElementById("categoryName").value;

	if (!categoryName) {
		divMessageCategory.classList.add("d-none");
		divMessageCategoryError.innerText = "Introduzca el nombre de la categoría";
		divMessageCategoryError.classList.remove("d-none");

		return;
	}

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
		showDeleteCategory();
	} else {
		divMessageCategory.classList.add("d-none");
		divMessageCategoryError.innerText = "El nombre solo pueden contener letras";
		divMessageCategoryError.classList.remove("d-none");

		return;
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
		showDeleteCategory();
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
function showDeleteCategory() {
	const inputCategoryName = document.getElementById('categoryName').value;
	const buttonDeleteCategory = document.getElementById('buttonDeleteCategory');

	if (inputCategoryName && regexOnlyWord.test(inputCategoryName)) {
		buttonDeleteCategory.classList.remove("d-none");
	} else {
		buttonDeleteCategory.classList.add("d-none");
	}
}

// Mostrar boton de eliminar Imagenes
function showDeleteImages() {
	const inputProductId = document.getElementById('productId');
	const buttonDeleteImages = document.getElementById('buttonDeleteImages');

	if (inputProductId.value) {
		buttonDeleteImages.classList.remove("d-none");
	} else {
		buttonDeleteImages.classList.add("d-none");
	}
}

// Mostrar boton de eliminar producto
function showDeleteProduct() {
	const inputProductId = document.getElementById('productId');
	const buttonDeleteProduct = document.getElementById('buttonDeleteProduct');

	if (inputProductId.value) {
		buttonDeleteProduct.classList.remove("d-none");
	} else {
		buttonDeleteProduct.classList.add("d-none");
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
function submitFormProduct(form) {
	divMessageProduct = document.getElementById('messageProduct');
	divMessageProductError = document.getElementById('messageProductError');
	const formData = new FormData();

	const productName = document.getElementById('productName').value;
	const productNumber = document.getElementById('productNumber').value;
	const productDescription = document.getElementById('productDescription').value;
	const selectedCategory = document.getElementById('productCategory').value;
	const productSize = document.getElementById('productSize').value;
	const productQuantity = document.getElementById('productQuantity').value;
	const files = document.getElementById('files').value;
	const iva = document.getElementById('iva').value;
	const basePrice = document.getElementById('basePrice').value;

	if (productName && productNumber && selectedCategory && selectedCategory
		&& productSize && productQuantity && files && iva && basePrice) {

		// Validar los valores usando el patrón
		const isValidProductName = onlyWordsNumbersSpaces.test(productName);
		const isValidProductDescription = onlyWordsNumbersSpaces.test(productDescription);
		const isValidProductSize = onlyWordsNumbersSpaces.test(productSize);
		const isValidIva = ivaRegex.test(iva);
		const isValidBasePrice = basePriceRegex.test(basePrice) && parseFloat(basePrice) >= 0.01;

		if (!isValidIva) {
			// Mensaje no cumple con el patron iva
			divMessageProduct.classList.add("d-none");
			divMessageProductError.innerText = "El valor para el IVA solo puede tener hasta dos dígitos";
			divMessageProductError.classList.remove("d-none");

			return;
		}

		if (!isValidBasePrice) {
			// Mensaje no cumple con el patron precio
			divMessageProduct.classList.add("d-none");
			divMessageProductError.innerText = "El valor mínimo para el precio base es 0.01";
			divMessageProductError.classList.remove("d-none");

			return;
		}

		if (!isValidProductName || !isValidProductDescription || !isValidProductSize) {
			// Mensaje no cumple con el patron
			divMessageProduct.classList.add("d-none");
			divMessageProductError.innerText = "Las entradas de datos solo permiten letras,números y espacios";
			divMessageProductError.classList.remove("d-none");

			return;
		}

		// Cargar imagenes
		if (uploadImages(productName)) {

			const imageUrls = document.getElementById('imageUrls').value;

			formData.append('productNumber', productNumber);
			formData.append('productName', productName);
			formData.append('productDescription', productDescription);
			formData.append('categoryId', selectedCategory);
			formData.append('productSize', productSize);
			formData.append('productQuantity', productQuantity);
			formData.append('imageUrls', imageUrls);
			formData.append('iva', iva);
			formData.append('basePrice', basePrice);


			fetch("products/save", {
				method: "POST",
				body: formData
			}).then(response => {
				if (response.status === 204) {
					divMessageProductError.classList.add("d-none");
					divMessageProduct.innerText = "El producto ha sido registrado";
					divMessageProduct.classList.remove("d-none");
				} else if (response.status === 422) {
					divMessageProduct.classList.add("d-none");
					divMessageProductError.innerText = "El nombre del producto introducido ya está registrado";
					divMessageProductError.classList.remove("d-none");
				} else {
					window.location.href = "/internalError";
				}
			}).catch(() => window.location.href = "/internalError");

			form.reset();

		} else {
			// Mostrar error de archivo no valido
			divMessageProduct.classList.add("d-none");
			divMessageProductError.innerText = "Ha cargado un archivo no válido, las extensiones permitidas son: 'jpg', 'jpeg', 'png', 'gif'";
			divMessageProductError.classList.remove("d-none");
		}
	} else {
		// Datos incompletas en el formulario
		divMessageProduct.classList.add("d-none");
		divMessageProductError.innerText = "Completa los datos obligatorios (*) en el formulario";
		divMessageProductError.classList.remove("d-none");
	}
}

// Cargar imagenes
function uploadImages(productName) {
	const inputFiles = document.getElementById('files');
	const inputUrlsHidden = document.getElementById('imageUrls');
	const formData = new FormData();
	let extensionException = false;
	// Lista de extensiones permitidas
	const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];

	// Agregar los archivos seleccionados al objeto FormData
	let i = 0;
	let result = "";
	for (const file of inputFiles.files) {
		i++;
		// Renombrar archivo
		// Obtener la extension del nombre original del archivo
		const extension = file.name.split('.').pop();

		if (allowedExtensions.includes(extension)) {

			// Comprobar si hay que poner coma
			if (result) {
				result = result + ',';
			}
			const newName = productName + i + '.' + extension;
			formData.append('files', file, newName);
			result = result + '/product_images/' + newName
		} else {
			extensionException = true;
			break;
		}
	}

	//Si las extensiones son permitidas continuar
	if (!extensionException) {

		// Guardar las imagenes en el sistema de ficheros del server
		fetch("products/upload", {
			method: "POST",
			body: formData
		}).then(response => {
			if (!response.ok) {
				window.location.href = "/internalError"
			}
		}).catch(() => window.location.href = "/internalError");

		// Guardar las url en el input oculto
		inputUrlsHidden.value = result;

		return result;
	} else {
		return "";
	}
}