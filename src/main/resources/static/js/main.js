//Expresiones regulares
const regexOnlyWord = /^[a-zA-ZÀ-ÖØ-öø-ÿ]*$/;
const onlyWordsNumbersSpaces = /^[a-zA-ZÀ-ÖØ-öø-ÿ\d\s]*$/;
const ivaRegex = /^\d{1,2}$/;
const basePriceRegex = /^(?:[1-9]\d*|0)?(?:\.\d+)?$/;
const numberProductRegex = /^\d{13}$/;
const separatorsRegex = /[\\/]/;

// Lista de extensiones permitidas
const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
// Url error
const urlError = "/internalError";


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
		showMessage(document.getElementById("passwordError"), "Las contraseñas no coinciden");
		return false;
	}

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
function saveCategory() {
	let form = document.getElementById("formCategory");
	let divMessageCategory = document.getElementById('messageCategory');
	let divMessageCategoryError = document.getElementById('messageCategoryError');
	let categoryName = document.getElementById("categoryName").value;

	if (!categoryName) {
		showMessage(divMessageCategoryError, "Introduzca el nombre de la categoría");

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
				showMessage(divMessageCategory, "La categoría se ha creado");
			} else if (response.status === 422) {
				showMessage(divMessageCategoryError, "Ya está registrada una categoría con ese nombre");
			} else {
				window.location.href = urlError;
			}
		}).catch(() => window.location.href = urlError);

		form.reset();

		// Mostrar boton de eliminar categoria
		showDeleteCategory();
	} else {
		showMessage(divMessageCategoryError, "El nombre solo pueden contener letras");
	}

}

// Buscar producto por el nombre
function searchProduct() {
	let formSearchProduct = document.getElementById('formSearchProduct');
	let divMessageSearchProductError = document.getElementById('messageSearchProductError');
	let productNum = document.getElementById("productNum").value;

	if (!productNum) {
		showMessage(divMessageSearchProductError, "Introduzca el número del producto");

		return;
	}

	if (numberProductRegex.test(productNum)) {
		fetch("products/searchByProductNumber/" + encodeURIComponent(productNum), {
			method: "GET",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			}
		}).then(response => {
			if (response.ok) {
				return response.json();
			} else {
				throw new Error("Error en la respuesta del servidor");
			}
		}).then(responseData => {

			if (responseData.productDto !== null && responseData.productDto.productId !== null) {
				document.getElementById('productId').value = responseData.productDto.productId;
				document.getElementById('productName').value = responseData.productDto.productName;
				document.getElementById('productNumber').value = responseData.productDto.productNumber;
				document.getElementById('productDescription').value = responseData.productDto.productDescription;
				document.getElementById('productSize').value = responseData.productDto.productSize;
				document.getElementById('productQuantity').value = responseData.productDto.productQuantity;
				document.getElementById('iva').value = responseData.productDto.iva;
				document.getElementById('basePrice').value = responseData.productDto.basePrice;
				// Definir el option selected
				let selectCategories = document.getElementById('productCategory');

				for (let i = 0; i < selectCategories.options.length; i++) {
					let option = selectCategories.options[i];
					// Verificar si el valor de la opcion coincide con el nombre de la categoría
					if (option.value == responseData.productDto.categoryId) {
						option.selected = true; // Establecer la opcion como seleccionada
						break;
					}
				}

				// Mostrar botones de eliminacion
				showDeleteProduct();
				showDeleteImages();
			} else {
				showMessage(divMessageSearchProductError, "No se ha encontrado ningún producto con ese nombre");
			}

			formSearchProduct.reset();
		}).catch(() => window.location.href = urlError);

	} else {
		showMessage(divMessageSearchProductError, "El número de producto debe tener 13 dígitos");

	}

}

// Comprobar si existe Categoria a eliminar
function deleteCategory() {
	let form = document.getElementById("formCategory");
	let divMessageCategoryError = document.getElementById('messageCategoryError');

	// Obtener el nombre de la categoria desde el campo de entrada
	categoryName = document.getElementById("categoryName").value;

	//Mensaje de confirmacion
	const result = confirm("¿Estás seguro que quieres eliminar la categoría?");

	if (result) {
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
					window.location.href = urlError
				}
			}).then(responseData => {
				// Manipular los datos de la respuesta
				if (responseData.categoryId !== null) {
					// Si se encontro la categoria, realizar la eliminacion
					deleteCategoryById(responseData.categoryId);
				} else {
					showMessage(divMessageCategoryError, "No se ha encontrado ninguna categoría con ese nombre");
				}
			}).catch(() => window.location.href = urlError);

			form.reset();

			// Mostrar boton de eliminar categoria
			showDeleteCategory();
		} else {
			showMessage(divMessageCategoryError, "El nombre solo pueden contener letras");
		}

	}

}

// Eliminar categoria por ID
function deleteCategoryById(categoryId) {
	let divMessageCategory = document.getElementById('messageCategory');

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
			showMessage(divMessageCategory, "Se ha eliminado correctamente la categoría");
		} else {
			window.location.href = urlError;
		}
	}).catch(() => window.location.href = urlError);
}

// Mostrar boton de eliminar categoria
function showDeleteCategory() {
	let inputCategoryName = document.getElementById('categoryName').value;
	let buttonDeleteCategory = document.getElementById('buttonDeleteCategory');

	if (inputCategoryName && regexOnlyWord.test(inputCategoryName)) {
		buttonDeleteCategory.classList.remove("d-none");
	} else {
		buttonDeleteCategory.classList.add("d-none");
	}
}

// Mostrar boton de eliminar Imagenes
function showDeleteImages() {
	let inputProductId = document.getElementById('productId');
	let buttonDeleteImages = document.getElementById('buttonDeleteImages');

	if (inputProductId.value) {
		buttonDeleteImages.classList.remove("d-none");
	} else {
		buttonDeleteImages.classList.add("d-none");
	}
}

// Mostrar boton de eliminar producto
function showDeleteProduct() {
	let inputProductId = document.getElementById('productId');
	let buttonDeleteProduct = document.getElementById('buttonDeleteProduct');

	if (inputProductId.value) {
		buttonDeleteProduct.classList.remove("d-none");
	} else {
		buttonDeleteProduct.classList.add("d-none");
	}
}

// Mostrar todas las categorias
function showCategories() {
	let selectCategories = document.getElementById('productCategory');

	fetch("category/searchAll", {
		method: "GET"
	})
		.then(response => {
			if (response.ok) {
				return response.json();
			} else {
				window.location.href = urlError
			}
		})
		.then(data => {
			if (data) {
				// Limpiar opciones existentes en el select
				selectCategories.innerHTML = "";

				// Agregar la opcion por defecto
				let defaultOption = document.createElement("option");
				defaultOption.value = "";
				defaultOption.text = "Seleccione";
				selectCategories.appendChild(defaultOption);

				// Iterar sobre los datos y agregar opciones al select
				data.productCategoryDto.forEach(category => {
					let option = document.createElement("option");
					option.value = category.categoryId;
					option.text = category.categoryName;
					selectCategories.appendChild(option);
				});
			}

		})
		.catch(() => window.location.href = urlError);
}

// Enviar formulario Producto
function submitFormProduct(form) {
	let productId = document.getElementById('productId').value;
	let divMessageProduct = document.getElementById('messageProduct');
	let divMessageProductError = document.getElementById('messageProductError');
	let productName = document.getElementById('productName').value;
	let productNumber = document.getElementById('productNumber').value;
	let productDescription = document.getElementById('productDescription').value;
	let selectedCategory = document.getElementById('productCategory').value;
	let productSize = document.getElementById('productSize').value;
	let productQuantity = document.getElementById('productQuantity').value;
	let files = document.getElementById('files').value;
	let iva = document.getElementById('iva').value;
	let basePrice = document.getElementById('basePrice').value;
	let formData = new FormData();

	if (productName && productNumber && selectedCategory && selectedCategory
		&& productSize && productQuantity && files && iva && basePrice) {

		// Validar los valores usando el patron
		let isValidProductName = onlyWordsNumbersSpaces.test(productName);
		let isValidProductDescription = onlyWordsNumbersSpaces.test(productDescription);
		let isValidProductSize = onlyWordsNumbersSpaces.test(productSize);
		let isValidIva = ivaRegex.test(iva);
		let isValidBasePrice = basePriceRegex.test(basePrice) && parseFloat(basePrice) >= 0.01;

		if (!isValidIva) {
			// Mensaje no cumple con el patron iva
			showMessage(divMessageProductError, "El valor para el IVA solo puede tener hasta dos dígitos");

			return;
		}

		if (!isValidBasePrice) {
			// Mensaje no cumple con el patron precio
			showMessage(divMessageProductError, "El valor mínimo para el precio base es 0.01");

			return;
		}

		if (!isValidProductName || !isValidProductDescription || !isValidProductSize) {
			// Mensaje no cumple con el patron
			showMessage(divMessageProductError, "Las entradas de datos solo permiten letras,números y espacios");

			return;
		}

		// Cargar imagenes
		if (uploadImages(productName, productId)) {

			let imageUrls = document.getElementById('imageUrls').value;

			if (productId) {
				formData.append('productId', productId);
			}
			formData.append('productNumber', productNumber);
			formData.append('productName', productName);
			formData.append('productDescription', productDescription);
			formData.append('categoryId', selectedCategory);
			formData.append('productSize', productSize);
			formData.append('productQuantity', productQuantity);
			formData.append('imageUrls', imageUrls);
			formData.append('iva', iva);
			formData.append('basePrice', basePrice);

			// Registrar o actualizar
			if (productId) {
				fetch("products/update", {
					method: "POST",
					body: formData
				}).then(response => {
					if (response.status === 204) {
						showMessage(divMessageProduct, "El producto ha sido actualizado");
					} else if (response.status === 422) {
						showMessage(divMessageProductError, "Ya existe otro producto con ese nombre");
					} else {
						window.location.href = urlError;
					}
				}).catch(() => window.location.href = urlError);
			} else {
				fetch("products/save", {
					method: "POST",
					body: formData
				}).then(response => {
					if (response.status === 204) {
						showMessage(divMessageProduct, "El producto ha sido registrado");
					} else if (response.status === 422) {
						showMessage(divMessageProductError, "El nombre del producto introducido ya está registrado");
					} else {
						window.location.href = urlError;
					}
				}).catch(() => window.location.href = urlError);
			}



			form.reset();

		} else {
			// Mostrar error de archivo no valido
			showMessage(divMessageProductError, "Ha cargado un archivo no válido, las extensiones permitidas son: 'jpg', 'jpeg', 'png', 'gif'");
		}
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageProductError, "Completa los datos obligatorios (*) en el formulario");
	}
}

// Cargar imagenes
function uploadImages(productName, productId) {
	let inputFiles = document.getElementById('files');
	let inputUrlsHidden = document.getElementById('imageUrls');
	let formData = new FormData();
	let extensionException = false;

	// Agregar los archivos seleccionados al objeto FormData
	let i = 0;
	let result = "";
	for (let file of inputFiles.files) {
		i++;
		// Renombrar archivo
		// Obtener la extension del nombre original del archivo
		let extension = file.name.split('.').pop();

		if (allowedExtensions.includes(extension)) {

			// Comprobar si hay que poner coma
			if (result) {
				result = result + ',';
			}
			let newName = productName + i + '.' + extension;
			formData.append('files', file, newName);
			result = result + newName
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
				window.location.href = urlError;
			}
		}).catch(() => window.location.href = urlError);

		// Guardar las url en el input oculto
		if (productId) {
			let onlyFileNames = extractFileName(inputUrlsHidden.value);
			onlyFileNames = onlyFileNames + ',' + result;
		} else {
			inputUrlsHidden.value = result;
		}


		return result;
	} else {
		return "";
	}
}

//Extraer nombres de archivos
function extractFileName(routes) {
	// Dividir las rutas por coma y eliminar espacios en blanco
	const separateRoutes = routes.split(',').map(route => route.trim());

	// Array para almacenar nombres de archivo
	const fileNames = [];

	// Iterar sobre cada ruta
	separateRoutes.forEach(ruta => {
		// Obtener el ultimo segmento de la ruta
		const fileName = ruta.split(separatorsRegex).pop();
		// Agregar el nombre de archivo a la lista si existe
		if (fileName) {
			fileNames.push(fileName);
		}
	});

	// Devolver los nombres de archivo separados por coma
	return fileNames.join(',');
}

// Mostrar alert
function showMessage(div, message) {
	div.innerText = message;
	div.classList.remove('d-none'); // Mostrar el elemento

	// Ocultar el mensaje despues de 5 segundos
	setTimeout(function() {
		div.classList.add('d-none'); // Ocultar el elemento nuevamente
	}, 5000);
}

// Eliminar producto
function deleteProduct() {
	let divProductId = document.getElementById("productId");
	let productId = divProductId.value;
	let form = document.getElementById("formProduct");
	let divMessageProduct = document.getElementById("messageProduct");

	//Mensaje de confirmacion
	const result = confirm("¿Estás seguro que quieres eliminar el producto?");

	if (result && productId) {
		fetch("products/delete/" + encodeURIComponent(productId), {
			method: "DELETE",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			}
		}).then(response => {
			if (response.ok) {
				showMessage(divMessageProduct, "Se ha eliminado correctamente el producto");
			} else {
				window.location.href = urlError;
			}
		}).catch(() => {
			window.location.href = urlError;
			return;
		});
	}

	// Ocultar botones de eliminar
	form.reset();
	divProductId.value = "";
	showDeleteImages();
	showDeleteProduct();
}

// Eliminar imagenes
function deleteImages() {
	let inputUrlsHidden = document.getElementById('imageUrls');
	let divProductId = document.getElementById("productId");
	let productId = divProductId.value;
	let form = document.getElementById("formProduct");
	let divMessageProduct = document.getElementById("messageProduct");

	//Mensaje de confirmacion
	const result = confirm("¿Estás seguro que quieres eliminar todas las imágenes del producto?");

	if (result && productId) {
		fetch("products/deleteImages/" + encodeURIComponent(productId), {
			method: "DELETE",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			}
		}).then(response => {
			if (response.ok) {
				showMessage(divMessageProduct, "Se han eliminado correctamente las imágenes");
			} else {
				window.location.href = urlError;
			}
		}).catch(() => {
			window.location.href = urlError;
			return;
		});
	}

	// Ocultar botones de eliminar
	form.reset();
	divProductId.value = "";
	inputUrlsHidden.value = "";
	showDeleteImages();
	showDeleteProduct();
}

