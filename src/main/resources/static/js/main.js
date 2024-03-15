document.addEventListener('DOMContentLoaded', function() {
	const adminLink = document.getElementById('adminLink');
	adminLink.addEventListener('click', function(event) {

		// Obtener el ancho de la ventana
		const windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;

		// Definir el ancho maximo para considerar como dispositivo movil
		const mobileWidth = 768;

		// Redireccionar si el ancho de la ventana es menor o igual al ancho definido para dispositivos moviles
		if (windowWidth <= mobileWidth) {
			event.preventDefault();
			window.location.href = "/redirect/responsiveAdmin";
		}

	});
});


// Obtener la altura del footer
const footHeight = document.getElementById('footer').clientHeight;
// Obtener la altura del header
const headHeight = document.getElementById('header').clientHeight;
// Ajustar la altura de la barra lateral restando la altura del footer
const sidebar = document.querySelector('.sidebar');
sidebar.style.height = `calc(100vh - ${footHeight}px - ${headHeight}px)`;