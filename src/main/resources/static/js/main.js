

// Deshabilitar administracion en dispositivo movil
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

// Comprobar inputs formulario de inscripcion de usuarios
document.addEventListener('DOMContentLoaded', function() {
	const name = document.getElementById('name');
	const surname = document.getElementById('surname');
	const secondSurname = document.getElementById('secondSurname');
	const passwdInput = document.getElementById('passwd');
	const dni = document.getElementById('dni');
	const phoneNumber = document.getElementById('phoneNumber');
	const email = document.getElementById('email');
	const confirmPasswdInput = document.getElementById('confirmPasswd');
	const passwordError = document.getElementById('passwordError');
	const submitButton = document.getElementById('submitButton');
	const question1 = document.getElementById('question1');
	const answer1 = document.getElementById('answer1');
	const question2 = document.getElementById('question2');
	const answer2 = document.getElementById('answer2');
	const question3 = document.getElementById('question3');
	const answer3 = document.getElementById('answer3');


	function checkFormValidity() {
		if (name.value && passwdInput.value && dni.value && surname.value && email.value && secondSurname.value 
		&& confirmPasswdInput.value && question1.value && answer1.value && phoneNumber.value
		&& question2.value && answer2.value && question3.value && answer3.value && passwdInput.value === confirmPasswdInput.value) {
			submitButton.disabled = false;
			passwordError.style.display = 'none';
		} else {
			submitButton.disabled = true;
			if (passwdInput.value !== confirmPasswdInput.value) {
				passwordError.style.display = 'block';
			} else {
				passwordError.style.display = 'none';
			}
		}
	}

	nombreInput.addEventListener('input', checkFormValidity);
	passwdInput.addEventListener('input', checkFormValidity);
	confirmPasswdInput.addEventListener('input', checkFormValidity);
});