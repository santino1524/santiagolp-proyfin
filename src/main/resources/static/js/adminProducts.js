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

// Convertir un array de blobs a un array de cadenas Base64
function blobsToBase64(blobs) {
	return new Promise((resolve, reject) => {
		const base64Images = [];

		const convertNextBlob = index => {
			if (index < blobs.length) {
				const reader = new FileReader();
				reader.onload = function() {
					const base64String = reader.result.split(',')[1];
					base64Images.push(base64String);
					convertNextBlob(index + 1);
				};
				reader.onerror = function(error) {
					reject(error);
				};
				reader.readAsDataURL(blobs[index]);
			} else {
				resolve(base64Images);
			}
		};

		convertNextBlob(0);
	});
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
		fetch("/category/save", {
			method: "POST",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			},
			body: JSON.stringify({
				categoryName: categoryName,
			})
		}).then(response => {
			if (response.status === 200) {
				showMessage(divMessageCategory, "La categoría se ha creado");
				return response.json();
			} else if (response.status === 422) {
				showMessage(divMessageCategoryError, "Ya está registrada una categoría con ese nombre");
			} else {
				window.location.href = urlError;
			}
		}).then(data => {
			if (data) {
				addOption(data.productCategoryDto.categoryId, data.productCategoryDto.categoryName);
			}
		}).catch((error) => {
			console.error(error);
			window.location.href = urlError;
		});

		form.reset();

		// Mostrar boton de eliminar categoria
		showDeleteCategory();
	} else {
		showMessage(divMessageCategoryError, "El nombre solo pueden contener letras");
	}
}

// Agregar option
function addOption(id, valor) {
	let selectElement = document.getElementById("productCategory");

	// Crear un nuevo elemento <option>
	let newOption = document.createElement("option");
	newOption.value = id;
	newOption.text = valor;

	// Agregar el nuevo <option> al final del <select>
	selectElement.appendChild(newOption);
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
		fetch("/products/searchByProductNumber/" + encodeURIComponent(productNum), {
			method: "GET",
		}).then(response => {
			if (response.ok) {
				return response.json();
			} else {
				window.location.href = urlError;
			}
		}).then(responseData => {

			if (responseData.productDto !== null && responseData.productDto.productId !== null) {
				document.getElementById('productId').value = responseData.productDto.productId;
				document.getElementById('productName').value = responseData.productDto.productName;
				document.getElementById('productNumber').value = responseData.productDto.productNumber;
				document.getElementById('productDescription').value = responseData.productDto.productDescription;
				document.getElementById('productSize').value = responseData.productDto.productSize;
				document.getElementById('foundImages').value = responseData.productDto.images;
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
		}).catch((error) => {
			console.error(error);
			window.location.href = urlError;
		});
	} else {
		showMessage(divMessageSearchProductError, "El número de producto debe tener 13 dígitos");

	}

}

// Comprobar si existe Categoria a eliminar
function deleteCategory() {
	//Mensaje de confirmacion
	document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres eliminar la categoría?";
	document.getElementById("modalAdminBtnOk").onclick = confirmDeleteCategory;
	$('#adminModal').modal('show');
}

// Confirmar eliminar categoria
function confirmDeleteCategory() {
	let form = document.getElementById("formCategory");
	let divMessageCategoryError = document.getElementById('messageCategoryError');

	// Obtener el nombre de la categoria desde el campo de entrada
	let categoryName = document.getElementById("categoryName").value;

	// Comprobar si el valor coincide con el patron
	if (regexOnlyWord.test(categoryName)) {
		fetch("/category/searchByName", {
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
				return response.json();
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
		}).catch((error) => {
			console.error(error);
			window.location.href = urlError;
		});
		form.reset();

		// Mostrar boton de eliminar categoria
		showDeleteCategory();
	} else {
		showMessage(divMessageCategoryError, "El nombre solo pueden contener letras");
	}
}

// Eliminar option del select Categorias
function deleteOption(id) {
	let selectElement = document.getElementById("productCategory");

	for (let i = 0; i < selectElement.options.length; i++) {
		let option = selectElement.options[i];

		// Verificar si el valor del <option> coincide con el ID
		if (option.value == id) {
			// Eliminar el <option> del <select>
			selectElement.remove(i);
			break;
		}
	}
}

// Eliminar categoria por ID
function deleteCategoryById(categoryId) {
	let divMessageCategory = document.getElementById('messageCategory');
	let divMessageCategoryError = document.getElementById('messageCategoryError');

	fetch("/category/delete", {
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
			deleteOption(categoryId);
		} else if (response.status === 422) {
			showMessage(divMessageCategoryError, "La categoría no se pudo eliminar porque está en uso");
		} else {
			window.location.href = urlError;
		}
	}).catch((error) => {
		console.error(error);
		window.location.href = urlError;
	});
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
	let images = document.getElementById('foundImages').value;

	if (inputProductId.value && images) {
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

	fetch("/category/searchAll", {
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
			if (data && data.productCategoryDto.length > 0) {
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

		}).catch((error) => {
			console.error(error);
			window.location.href = urlError;
		});

}

// Enviar formulario Producto
async function submitFormProduct(form) {
	successfulSave = 0;
	let productId = document.getElementById('productId').value;
	let divMessageProductError = document.getElementById('messageProductError');
	let productName = document.getElementById('productName').value;
	let productNumber = document.getElementById('productNumber').value;
	let productDescription = document.getElementById('productDescription').value;
	let selectedCategory = document.getElementById('productCategory').value;
	let productSize = document.getElementById('productSize').value;
	let productQuantity = document.getElementById('productQuantity').value;
	let iva = document.getElementById('iva').value;
	let basePrice = document.getElementById('basePrice').value;
	let images = document.getElementById('images').files;
	let formData = new FormData();

	if (productName && productNumber && selectedCategory && selectedCategory
		&& productSize && productQuantity && images && iva && basePrice) {

		// Validar los valores usando el patron
		let isValidProductName = onlyWordsNumbersSpaces.test(productName);
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

		if (!isValidProductName || !isValidProductSize) {
			// Mensaje no cumple con el patron
			showMessage(divMessageProductError, "Las entradas de datos solo permiten letras,números y espacios");

			return;
		}

		if (productId) {
			formData.append('productId', productId);
		}
		formData.append('productNumber', productNumber);
		formData.append('productName', productName);
		formData.append('productDescription', productDescription);
		formData.append('categoryId', selectedCategory);
		formData.append('productSize', productSize);
		formData.append('productQuantity', productQuantity);
		formData.append('iva', iva);
		formData.append('basePrice', basePrice);
		let imagesBase64 = await blobsToBase64(images);
		for (let image of imagesBase64) {
			formData.append('images', image)
		}

		// Registrar o actualizar
		if (productId) {
			await updateProduct(formData);
		} else {
			productId = await saveProduct(formData);
		}

		document.getElementById('productId').value = "";
		form.reset();
		showDeleteImages();
		showDeleteProduct();
	} else {
		// Datos incompletas en el formulario
		showMessage(divMessageProductError, "Completa los datos obligatorios (*) en el formulario");
	}
}

// Peticion para actualizar producto
async function updateProduct(formData) {
	let divMessageProduct = document.getElementById("messageProduct");
	
	try {
		let response = await fetch("/products/update", {
			method: "POST",
			body: formData
		});

		if (response.status === 200) {
			showMessage(divMessageProduct, "El producto ha sido actualizado");
		} else if (response.status === 422) {
			showMessage(divMessageProductError, "El nombre del producto introducido ya está registrado");
			return;
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Peticion para guardar producto
async function saveProduct(formData) {
	let divMessageProduct = document.getElementById("messageProduct");
	let data;

	try {
		let response = await fetch("/products/save", {
			method: "POST",
			body: formData
		});

		if (response.status === 200) {
			showMessage(divMessageProduct, "El producto ha sido registrado");
			data = await response.json();
		} else if (response.status === 422) {
			showMessage(divMessageProductError, "El nombre del producto introducido ya está registrado");
			return;
		} else {
			window.location.href = urlError;
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}

	if (data) {
		return data.productId;
	}
}

// Verificar tamanyo
function verifySize() {
	let inputFiles = document.getElementById('images');
	let divMessageProductError = document.getElementById('messageProductError');

	for (let i = 0; i < inputFiles.files.length; i++) {
		let file = inputFiles.files[i];
		let fileSize = file.size;

		if (fileSize > maxSizeInBytes) {
			// Mostrar error de archivo no valido
			showMessage(divMessageProductError, "Hay imágenes que exceden el peso máximo permitido (500KB)");

			// Limpiar el input de archivo para evitar enviar archivos demasiado grandes
			inputFiles.value = '';

			return;
		}
	}
}

// Verificar ficheros
function verifyFiles() {
	verifyExtension();
	verifySize();
}

// Verificar extensiones
function verifyExtension() {
	let inputFiles = document.getElementById('images');
	let divMessageProductError = document.getElementById('messageProductError');

	// Recorremos los archivos seleccionados
	for (let i = 0; i < inputFiles.files.length; i++) {
		let file = inputFiles.files[i];
		let filename = file.name;

		// Obtenemos la extension del archivo
		let extension = filename.split('.').pop().toLowerCase();

		// Verificamos si la extension esta en la lista de extensiones permitidas
		if (allowedExtensions.indexOf(extension) === -1) {
			// Mostrar error de archivo no valido
			showMessage(divMessageProductError, "La carga de imágenes contiene extensiones no válidas (extensiones permitidas son: 'jpg', 'jpeg', 'png', 'gif')");

			// Limpiar el input de archivo para evitar enviar archivos demasiado grandes
			inputFiles.value = '';

			return;
		}
	}
}

// Eliminar producto
function deleteProduct() {
	//Mensaje de confirmacion
	document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres eliminar el producto?";
	document.getElementById("modalAdminBtnOk").onclick = confirmDeleteProduct;
	$('#adminModal').modal('show');
}

// Eliminar producto confirmado
function confirmDeleteProduct() {
	let divProductId = document.getElementById("productId");
	let productId = divProductId.value;
	let form = document.getElementById("formProduct");
	let divMessageProduct = document.getElementById("messageProduct");

	if (productId) {
		fetch("/products/delete/" + productId, {
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
		}).catch((error) => {
			console.error(error);
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

	//Mensaje de confirmacion
	document.getElementById("adminModalBody").innerHTML = "¿Estás seguro que quieres eliminar todas las imágenes del producto?";
	document.getElementById("modalAdminBtnOk").onclick = confirmDeleteImages;
	$('#adminModal').modal('show');
}

// Eliminar imagenes confirmado
function confirmDeleteImages() {
	let images = document.getElementById('foundImages');
	let divProductId = document.getElementById("productId");
	let productId = divProductId.value;
	let divMessageProduct = document.getElementById("messageProduct");

	if (productId) {
		fetch("/products/deleteImages/" + productId, {
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
		}).catch((error) => {
			console.error(error);
			window.location.href = urlError;
			return;
		});
	}

	// Ocultar botones de eliminar
	images.value = "";
	showDeleteImages();
	showDeleteProduct();
}


