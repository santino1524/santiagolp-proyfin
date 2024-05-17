// Maquetar categorias con productos
async function layoutCategoriesProducts(categories) {
	let divContainer = document.getElementById('containerIndex');

	for (const category of categories) {
		// Insertar Nombre de categoria
		let aCategory = document.createElement('a');
		aCategory.classList.add('text-center', 'mb-3', 'mt-3', 'category-name');
		aCategory.href = "/products/searchByCategoryPageProducts/" + category.categoryId;
		aCategory.style.textDecoration = 'none';
		let h2Category = document.createElement('h2');
		h2Category.textContent = category.categoryName;
		aCategory.append(h2Category);
		divContainer.append(aCategory);

		// Contenedor de productos 
		let divContainerProducts = document.createElement('div');
		divContainerProducts.classList.add('row', 'my-3', 'border-top', 'border-bottom', 'py-2');

		// Buscar productos de esa categoria     
		let products = await searchProductsByCategory(category.categoryId);
		let productCounter = 0;
		for (const product of products) {

			// Salir del bucle si ya se han mostrado cuatro productos
			if (productCounter >= 4) {
				break;
			}

			// Card
			let divProducts = document.createElement('div');
			divProducts.classList.add('col-md-3', 'col-sm-6');

			let divProductsGrid = document.createElement('div');
			divProductsGrid.classList.add('product-grid');

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
			let img2 = document.createElement('img');
			img2.classList.add('pic-2');
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
			if (product.reviewsDto) {
				// Calcular promedio rating
				let sum = 0;
				let reviews = product.reviewsDto.length;
				for (let i = 0; i < reviews; i++) {
					sum += product.reviewsDto[i].rating;
				}
				ave = parseInt(sum / reviews);

				for (let i = 0; i < ave; i++) {
					let li = document.createElement('li');
					li.classList.add('fas', 'fa-star');
					ul.append(li);
				}
			}
			let aCart = document.createElement('a');
			aCart.classList.add('add-to-cart');
			aCart.href = '#';
			aCart.onclick = function() {
				addCart(product.productId, 1);
			};
			aCart.append('Añadir al carrito');
			divProductsRating.append(ul);
			divProductsRating.append(aCart);
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

			// Incrementar el contador de productos mostrados
			productCounter++;
		}

		// Conformar categoria con productos
		divContainer.append(divContainerProducts);

		loaderDeactivate();
	}
}

// Cargar index
async function loadIndex() {
	try {
		let response = await fetch("/category/searchAll", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.productCategoryDto.length > 0) {
			// Maquetar categorias con productos
			layoutCategoriesProducts(data.productCategoryDto);
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Buscar productos por categoria
async function searchProductsByCategory(categoryId) {
	try {
		let response = await fetch("/products/searchByCategory?categoryId=" + categoryId, {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();
		return data.products;
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}