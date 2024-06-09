let oneCategory;
let currentIdCategory;
let mapCategories = {};

// Maquetar productos
async function layoutProducts(products) {
	loaderActive();

	// Obtener categorias
	await listCategories();

	let divContainer = document.getElementById('containerIndex');
	document.getElementById('msgNotFound').classList.add('d-none');
	document.getElementById('not-found').classList.add('d-none');
	oneCategory = true;
	currentIdCategory = products[0].categoryId;

	// Limpiar productos existentes
	divContainer.innerHTML = '';

	// Contenedor de productos 
	let divContainerProducts = document.createElement('div');
	divContainerProducts.classList.add('row', 'my-3', 'py-2');

	// Buscar productos
	for (const product of products) {

		if (product.productQuantity >= 0) {

			// Verificar si el categoryId de este producto es diferente al del primer producto
			if (product.categoryId !== currentIdCategory && oneCategory) {
				oneCategory = false;
			}

			// Card
			let divProducts = document.createElement('div');
			divProducts.classList.add('col-md-3', 'col-sm-6');

			let divProductsGrid = document.createElement('div');
			divProductsGrid.classList.add('product-grid', 'mb-4');

			// Imagenes
			let divProductsImage = document.createElement('div');
			divProductsImage.classList.add('product-image');
			let aImage = document.createElement('a');
			aImage.classList.add('image');
			aImage.href = '#';
			aImage.onclick = () => {
				// Mostrar Modal con Product
				showModalProduct(product);
			};
			let img1 = document.createElement('img');
			img1.classList.add('pic-1');
			img1.alt = `Producto ${product.productNumber}`;
			img1.title = `Producto ${product.productNumber}`;
			let img2 = document.createElement('img');
			img2.classList.add('pic-2');
			img2.alt = `Producto ${product.productNumber}`;
			img2.title = `Producto ${product.productNumber}`;
			// Crea una URL de datos (data URL) 
			let dataUrl = 'data:image/jpeg;base64,' + product.images[0];
			let dataUrl2;
			let sizeImages = product.images.length
			if (sizeImages > 1) {
				dataUrl2 = 'data:image/jpeg;base64,' + product.images[sizeImages - 1];
				img1.src = dataUrl;
				img2.src = dataUrl2;
			} else {
				img1.src = dataUrl;
				img2.src = dataUrl;
			}
			aImage.append(img1);
			aImage.append(img2);
			divProductsImage.append(aImage);

			// Rating y boton carrito
			let divProductsRating = document.createElement('div');
			divProductsRating.classList.add('product-rating');
			let ul = document.createElement('ul');
			ul.classList.add('rating');

			let sum = 0;
			if (product.reviewsDto && product.reviewsDto.length > 0) {
				// Calcular promedio rating
				let reviews = 0;
				for (let i = 0; i < product.reviewsDto.length; i++) {
					let review = product.reviewsDto[i];
					if (review.rating > 0 && !review.reported) {
						reviews++;
						sum += product.reviewsDto[i].rating;
					}
				}

				// Si hay resennas
				if (sum) {
					ave = sum / reviews;

					for (let i = 0; i < parseInt(ave); i++) {
						let li = document.createElement('li');
						li.classList.add('fas', 'fa-star');
						ul.append(li);
					}
				}
			}

			if (product.productQuantity > 0 && ! await isGokuUser()) {
				let aCart = document.createElement('a');
				aCart.classList.add('add-to-cart');
				aCart.href = '#';
				aCart.onclick = function() {
					addCart(product.productId, 1);
				};
				aCart.append('Añadir al carrito');
				divProductsRating.append(aCart);
			}

			divProductsRating.append(ul);
			divProductsImage.append(divProductsRating);
			divProductsGrid.append(divProductsImage);

			// Producto y precio
			let divProductContent = document.createElement('div');
			divProductContent.classList.add('product-content');
			let h3Content = document.createElement('h3');
			h3Content.classList.add('title');
			let aTitle = document.createElement('a');
			aTitle.href = '#';
			aTitle.onclick = () => {
				// Mostrar Modal con Product
				showModalProduct(product);
			};
			aTitle.append(product.productName);
			let divPrice = document.createElement('div');
			divPrice.classList.add('price');
			divPrice.append(product.pvpPrice + '€');
			h3Content.append(aTitle);
			divProductContent.append(h3Content);
			divProductContent.append(divPrice);
			divProductsGrid.append(divProductContent);

			// Conformar card
			divProducts.append(divProductsGrid);
			divContainerProducts.append(divProducts);
			divContainer.append(divContainerProducts);
		}
	}

	// Conformar categoria con productos
	divContainer.append(divContainerProducts);

	loaderDeactivate();

	// Establecer nombre de select de categorias
	if (oneCategory) {
		document.getElementById('dropdownMenuButton').textContent = mapCategories[currentIdCategory];
	}
}

// Listar categorias en select
async function listCategories() {
	let dropdownMenu = document.getElementById('listCategoriesProducts');

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

			// Iterar sobre las categorias y agregarlas como elementos <li>
			data.productCategoryDto.forEach(category => {
				let li = document.createElement('li');
				let a = document.createElement('a');
				a.classList.add('dropdown-item');
				a.href = "#";

				// Agregar la funcion en el evento onclick
				a.onclick = function(event) {
					let divContainer = document.getElementById('containerIndex');
					let optionSortDesc = document.getElementById('optionSortDesc');
					let optionSortAsc = document.getElementById('optionSortAsc');
					let productName = document.getElementById('inputSearch').value;
					event.preventDefault();

					if ((optionSortDesc.checked || optionSortAsc.checked) && !productName) {
						// buscar todo por categoria y ordenar 
						if (optionSortDesc.checked) {
							searchByProductCategoryDesc(category.categoryId);
						} else {
							searchByProductCategoryAsc();
						}
					} else if ((optionSortDesc.checked || optionSortAsc.checked) && productName && divContainer.innerHTML) {
						// buscar todo por categoria y nombre y ordenar 
						if (optionSortDesc.checked) {
							searchByNameAndProductCategoryOrderDesc(productName, category.categoryId);
						} else {
							searchByNameAndProductCategoryOrderAsc(productName, category.categoryId);
						}
					} else if (!(optionSortDesc.checked && optionSortAsc.checked) && productName && divContainer.innerHTML) {
						searchByNameAndProductCategory(productName, category.categoryId);
					} else {
						// Buscar solo por categoria
						searchByCategory(category.categoryId);
					}
				};

				a.textContent = category.categoryName;
				li.appendChild(a);

				dropdownMenu.appendChild(li);

				// Agregar al mapa
				mapCategories[category.categoryId] = category.categoryName;
			});
		}
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Boton buscar por nombre
function searchByNameButton() {
	let productName = document.getElementById('inputSearch').value;
	let optionSortDesc = document.getElementById('optionSortDesc');
	let optionSortAsc = document.getElementById('optionSortAsc');

	if ((optionSortDesc.checked || optionSortAsc.checked) && !productName) {
		// buscar todo por categoria y ordenar 
		if (optionSortDesc.checked) {
			searchAllSortDesc();
		} else {
			searchAllSortDesc();
		}
	} else if ((optionSortDesc.checked || optionSortAsc.checked) && productName && oneCategory) {
		// buscar todo por categoria y nombre y ordenar 
		if (optionSortDesc.checked) {
			searchByNameAndProductCategoryOrderDesc(productName, currentIdCategory);
		} else {
			searchByNameAndProductCategoryOrderAsc(productName, currentIdCategory);
		}
	} else if ((optionSortDesc.checked || optionSortAsc.checked) && productName && !oneCategory) {
		// buscar todo por nombre y ordenar 
		if (optionSortDesc.checked) {
			searchByProductNameDesc(productName);
		} else {
			searchByProductNameAsc(productName);
		}
	} else if (!(optionSortDesc.checked && optionSortAsc.checked) && productName && oneCategory) {
		searchByNameAndProductCategory(productName, currentIdCategory);
	} else {
		searchByName(productName);
	}
}

// Buscar por nombre de producto
async function searchByName(productName) {
	try {
		let response = await fetch("/products/searchByProductName?productName=" + encodeURIComponent(productName), {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Limpiar filtros
function clearFilters() {
	document.getElementById('optionSortDesc').checked = false;
	document.getElementById('optionSortAsc').checked = false;
	document.getElementById('inputSearch').value = "";
	document.getElementById('dropdownMenuButton').textContent = "Categorías de productos";

	//Buscar todos los productos
	searchAllProducts();
}

// Mostrar imagen para productos no enconstrados
function showProductsNotFound() {
	document.getElementById('not-found').classList.remove('d-none');
}

// Iniciar busqueda
function handleKeyPress(event) {
	if (event.key === "Enter") {
		// Llamar a la funcion de búsqueda
		searchByNameButton();

		return false;
	}
	return true;
}

// Activar OptionSortDesc
function checkOptionSortDesc() {
	document.getElementById('optionSortDesc').checked = true;
	let divContainer = document.getElementById('containerIndex');
	let productName = document.getElementById('inputSearch').value;

	if (!productName && divContainer.innerHTML && oneCategory) {
		// Ordenar por categoria 
		searchByProductCategoryDesc(currentIdCategory);
	} else if (productName && !oneCategory) {
		// Ordenar por nombre
		searchByProductNameDesc(productName);
	} else if (productName && oneCategory) {
		// Ordenar por nombre y categoria
		searchByNameAndProductCategoryOrderDesc(productName, currentIdCategory);
	} else {
		// Ordenar todo
		searchAllSortDesc();
	}
}

// Activar OptionSortAsc
function checkOptionSortAsc() {
	document.getElementById('optionSortAsc').checked = true;
	let divContainer = document.getElementById('containerIndex');
	let productName = document.getElementById('inputSearch').value;

	if (!productName && divContainer.innerHTML && oneCategory) {
		// Ordenar por categoria 
		searchByProductCategoryAsc(currentIdCategory);
	} else if (productName && !oneCategory) {
		// Ordenar por nombre
		searchByProductNameAsc(productName);
	} else if (productName && oneCategory) {
		// Ordenar por nombre y categoria
		searchByNameAndProductCategoryOrderAsc(productName, currentIdCategory);
	} else {
		// Ordenar todo
		searchAllSortAsc();
	}
}

// Buscar todos SortDesc
async function searchAllSortDesc() {
	try {
		let response = await fetch("/products/searchAllProductDesc", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar todos los productos
async function searchAllProducts() {
	try {
		let response = await fetch("/products/searchAll", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar todos sortAsc
async function searchAllSortAsc() {
	try {
		let response = await fetch("/products/searchAllProductAsc", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por categoria SortDesc
async function searchByProductCategoryDesc(categoryId) {
	try {
		let response = await fetch("/products/searchByProductCategoryDesc?categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por categoria SortAsc
async function searchByProductCategoryAsc(categoryId) {
	try {
		let response = await fetch("/products/searchByProductCategoryAsc?categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por nombre SortDesc
async function searchByProductNameDesc(productName) {
	try {
		let response = await fetch("/products/searchByProductNameDesc?productName=" + encodeURIComponent(productName), {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por nombre y categoria SortDesc
async function searchByNameAndProductCategoryOrderDesc(productName, categoryId) {
	try {
		let response = await fetch("/products/searchByNameAndProductCategoryOrderDesc?productName=" + encodeURIComponent(productName) + "&categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por categoria
async function searchByCategory(categoryId) {
	try {
		let response = await fetch("/products/searchByCategory?categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por nombre y categoria SortAsc
async function searchByNameAndProductCategoryOrderAsc(productName, categoryId) {
	try {
		let response = await fetch("/products/searchByNameAndProductCategoryOrderAsc?productName=" + encodeURIComponent(productName) + "&categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por nombre y categoria
async function searchByNameAndProductCategory(productName, categoryId) {
	try {
		let response = await fetch("/products/searchByNameAndProductCategory?productName=" + encodeURIComponent(productName) + "&categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar por nombre SortAsc
async function searchByProductNameAsc(productName) {
	try {
		let response = await fetch("/products/searchByProductNameAsc?productName=" + encodeURIComponent(productName), {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.products.length > 0) {
			// Maquetar productos
			layoutProducts(data.products);
		} else {
			// Mostrar mensaje de productos no encontrados
			document.getElementById('containerIndex').innerHTML = '';
			document.getElementById('msgNotFound').classList.remove('d-none');
			showProductsNotFound();
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}