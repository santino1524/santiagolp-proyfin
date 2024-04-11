// Variables globales
let flagSidebar = true;

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

// Comprobacion de contrasennas al enviar formulario 
document.addEventListener("DOMContentLoaded", function() {
	var submitButton = document.getElementById("submitButtonRegister");
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
	var password = document.getElementById("passwd").value;
	var confirmPassword = document.getElementById("confirmPasswd").value;
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

