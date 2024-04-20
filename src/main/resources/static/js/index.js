// Maquetar categorias con productos
async function layoutCategoriesProducts(categories) {
	let divContainer = document.getElementById('containerIndex');

	for (const category of categories) {
		// Insertar Nombre de categoria
		let h2Category = document.createElement('h2');
		h2Category.classList.add('text-center', 'mb-3', 'mt-3');
		h2Category.textContent = category.categoryName;
		divContainer.append(h2Category);

		// Contenedor de productos 
		let divContainerProducts = document.createElement('div');
		divContainerProducts.classList.add('row', 'my-3', 'border-top', 'border-bottom', 'py-2');

		// Buacar productos de esa categoria     
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
			divProductsGrid.style.fontFamily = "'Roboto', sans-serif";
			divProductsGrid.style.textAlign = "center";
			divProductsGrid.style.transition = "all 0.5s";
			// Agregar sombra al hacer hover
			divProductsGrid.addEventListener('mouseenter', function() {
				divProductsGrid.style.boxShadow = "0 5px 18px rgba(0, 0, 0, 0.3)";
			});
			// Remover sombra al dejar de hacer hover
			divProductsGrid.addEventListener('mouseleave', function() {
				divProductsGrid.style.boxShadow = "none";
			});

			// Imagenes
			let divProductsImage = document.createElement('div');
			divProductsImage.classList.add('product-image');
			divProductsImage.style.position = "relative";
			divProductsImage.style.overflow = "hidden";
			let aImage = document.createElement('a');
			aImage.classList.add('image');
			aImage.href = '#'; // PONER URL DEV
			aImage.style.display = "block";
			let img1 = document.createElement('img');
			img1.classList.add('pic-1');
			img1.style.width = "100%";
			img1.style.height = "auto";
			img1.style.opacity = "1";
			img1.style.backfaceVisibility = "hidden";
			img1.style.transition = "all 0.5s";
			let img2 = document.createElement('img');
			img2.classList.add('pic-2');
			img2.style.width = "100%";
			img2.style.height = "100%";
			img2.style.opacity = "0";
			img2.style.transition = "all 0.5s";
			img2.style.position = "absolute";
			img2.style.top = "0";
			img2.style.left = "0";
			if (product.imageUrls.includes(',')) {
				img1.src = product.imageUrls.split(',')[0];
				img2.src = product.imageUrls.split(',').pop();
			} else {
				img1.src = product.imageUrls;
				img2.src = product.imageUrls;
			}
			// Agregar evento de mouseenter al contenedor .product-grid
			divProductsGrid.addEventListener('mouseenter', function() {
				// Obtener la imagen dentro del contenedor .product-image
				let pic1 = divProducts.querySelector('.product-image .pic-1');
				// Aplicar la opacidad
				if (pic1) {
					pic1.style.opacity = "0";
				}
			});
			// Agregar evento de mouseleave al contenedor .product-grid
			divProductsGrid.addEventListener('mouseleave', function() {
				// Obtener la imagen dentro del contenedor .product-image
				let pic1 = divProducts.querySelector('.product-image .pic-1');
				// Restaurar la opacidad
				if (pic1) {
					pic1.style.opacity = "1";
				}
			});
			// Agregar evento de mouseenter al contenedor .product-grid
			divProductsGrid.addEventListener('mouseenter', function() {
				// Obtener la imagen dentro del contenedor .product-image
				let pic2 = divProducts.querySelector('.product-image .pic-2');
				// Aplicar la opacidad
				if (pic2) {
					pic2.style.opacity = "1";
				}
			});
			// Agregar evento de mouseleave al contenedor .product-grid
			divProductsGrid.addEventListener('mouseleave', function() {
				// Obtener la imagen dentro del contenedor .product-image
				let pic2 = divProducts.querySelector('.product-image .pic-2');
				// Restaurar la opacidad
				if (pic2) {
					pic2.style.opacity = "0";
				}
			});
			aImage.append(img1);
			aImage.append(img2);
			divProductsImage.append(aImage);

			// Rating y boton carrito
			let divProductsRating = document.createElement('div');
			divProductsRating.classList.add('product-rating');
			divProductsRating.style.background = "rgba(255,255,255,0.95)";
			divProductsRating.style.width = "100%";
			divProductsRating.style.padding = "10px";
			divProductsRating.style.opacity = "0";
			divProductsRating.style.position = "absolute";
			divProductsRating.style.bottom = "-60px";
			divProductsRating.style.left = "0";
			divProductsRating.style.transition = "all .2s ease-in-out 0s";
			let ul = document.createElement('ul');
			ul.classList.add('rating');
			ul.style.padding = '0';
			ul.style.margin = '0';
			ul.style.listStyle = 'none';
			ul.style.float = 'left';
			let li = document.createElement('li');
			li.classList.add('fas', 'fa-star');
			li.style.color = '#6DA84A';
			li.style.fontSize = '13px';
			if (product.reviewsDto) {
				for (let i = 0; i < product.reviewsDto.rating; i++) {
					ul.append(li);
				}
			}
			let aCart = document.createElement('a');
			aCart.classList.add('add-to-cart');
			aCart.style.textDecoration = 'none';
			aCart.style.color = '#6DA84A';
			aCart.style.fontSize = '14px';
			aCart.style.fontWeight = '600';
			aCart.style.borderBottom = '1px solid #6DA84A';
			aCart.style.float = 'right';
			aCart.style.transition = 'all .2s ease-in-out 0s';
			aCart.href = '#'; // PONER URL DEV
			// Agregar evento de mouseenter al elemento <a>
			aCart.addEventListener('mouseenter', function() {
				// Cambiar color y borde al realizar hover
				aCart.style.color = '#000';
				aCart.style.borderColor = '#000';
			});
			// Agregar evento de mouseleave al elemento <a>
			aCart.addEventListener('mouseleave', function() {
				// Restaurar color y borde al salir del hover
				aCart.style.color = '#6DA84A';
				aCart.style.borderColor = '#6DA84A';
			});
			aCart.onclick = function() {
				addCart(product.productId);
			};
			aCart.append('Añadir al carrito');
			// Agregar evento de mouseenter al contenedor .product-grid
			divProductsGrid.addEventListener('mouseenter', function() {
				// Obtener el elemento .product-rating dentro del contenedor .product-grid
				let productRating = divProducts.querySelector('.product-rating');
				// Aplicar los estilos de opacidad y posición
				if (productRating) {
					productRating.style.opacity = "1";
					productRating.style.bottom = "0";
				}
			});
			// Agregar evento de mouseleave al contenedor .product-grid
			divProductsGrid.addEventListener('mouseleave', function() {
				// Obtener el elemento .product-rating dentro del contenedor .product-grid
				let productRating = divProducts.querySelector('.product-rating');
				// Restaurar los estilos de opacidad y posición
				if (productRating) {
					productRating.style.opacity = "0";
					productRating.style.bottom = "-60px";
				}
			});
			divProductsRating.append(ul);
			divProductsRating.append(aCart);
			divProductsImage.append(divProductsRating);
			divProductsGrid.append(divProductsImage);

			// Producto y precio
			let divProductContent = document.createElement('div');
			divProductContent.classList.add('product-content');
			divProductContent.style.background = "#F5F5F5";
			divProductContent.style.padding = "15px";
			let h3Content = document.createElement('h3');
			h3Content.classList.add('title');
			h3Content.style.fontSize = '18px';
			h3Content.style.textTransform = 'capitalize';
			h3Content.style.margin = '0 0 5px';
			let aTitle = document.createElement('a');
			aTitle.href = '#'; // PONER URL DEV
			aTitle.style.color = '#111';
			aTitle.style.transition = 'all 500ms';
			aTitle.style.textDecoration = 'none';
			// Agregar evento de mouseenter al elemento <a>
			aTitle.addEventListener('mouseenter', function() {
				// Cambiar color al realizar hover
				aTitle.style.color = '#6DA84A';
			});
			// Agregar evento de mouseleave al elemento <a>
			aTitle.addEventListener('mouseleave', function() {
				// Restaurar color al salir del hover
				aTitle.style.color = '#111';
			});
			aTitle.append(product.productName);
			let divPrice = document.createElement('div');
			divPrice.classList.add('price');
			divPrice.style.color = '#707070';
			divPrice.style.fontSize = '17px';
			divPrice.style.textDecoration = 'underline';
			divPrice.append(product.pvpPrice + '€');
			h3Content.append(aTitle);
			divProductContent.append(h3Content);
			divProductContent.append(divPrice);
			divProductsGrid.append(divProductContent);

			// Conformar card
			divProducts.append(divProductsGrid);
			divContainerProducts.append(divProducts);
			divContainer.append(divContainerProducts);

			// Verificar el ancho de la pantalla
			if (window.matchMedia("(max-width: 990px)").matches) {
				// Crear el estilo para .product-grid
				let style = document.createElement('style');
				style.innerHTML = `.product-grid {
           	 							margin-bottom: 40px;
        							}`;
				// Agregar el estilo al head del documento
				document.head.appendChild(style);
			}

			// Incrementar el contador de productos mostrados
			productCounter++;

		}

		// Conformar categoria con productos
		divContainer.append(h2Category);
		divContainer.append(divContainerProducts);
	}
}

// Buscar productos por categoria
async function searchProductsByCategory(categoryId) {
	try {
		let response = await fetch("products/searchByCategory/" + encodeURIComponent(categoryId), {
			method: "GET",
			headers: {
				"Content-type": "application/json; charset=utf-8"
			}
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();
		return data.productsDto;
	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}

// Annadir al carrito
function addCart(productId) {

}

// Cargar index
async function loadIndex() {
	try {
		let response = await fetch("category/searchAll", {
			method: "GET",
		});

		if (!response.ok) {
			window.location.href = urlError;
		}

		let data = await response.json();

		if (data && data.productCategoryDto[0].categoryId) {
			// Maquetar categorias con productos
			layoutCategoriesProducts(data.productCategoryDto);
		}

	} catch (error) {
		console.error(error);
		window.location.href = urlError;
	}
}
