//Expresiones regulares
const regexOnlyWord = /^[a-zA-ZÀ-ÖØ-öø-ÿ]*$/;
const onlyWordsNumbersSpaces = /^[a-zA-ZÀ-ÖØ-öø-ÿ\d\s.,;:]*$/;
const ivaRegex = /^\d{1,2}$/;
const basePriceRegex = /^(?:[1-9]\d*|0)?(?:\.\d+)?$/;
const numberProductRegex = /^\d{13}$/;
const separatorsRegex = /[\\/]/;
// Lista de extensiones permitidas
const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif'];
// Url error
const urlError = "/internalError";

// Comprobacion de contrasennas al enviar formulario de registro
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

	// Ocultar el mensaje despues de 5 segundos
	setTimeout(function() {
		div.classList.add('d-none'); // Ocultar el elemento nuevamente
	}, 5000);
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

		if (data) {
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

// Annadir al carrito
function addCart(productId) {

}

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