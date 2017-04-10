function verify() {

	var lastnameVal = document.form.lastname.value;
	var firstnameVal = document.form.firstname.value;
	var sexVal = document.form.sex.value;
	var regisVal = document.form.registrationcgi.value;
	var loginVal = document.form.login.value;
	var passwordVal = document.form.password.value;
	var enableVal = document.form.enable.value;
	
	if (/^[a-z ,.'-]+$/i.test(lastnameVal)){
	}
	else{
		document.form.lastname.style.backgroundColor = "red";
		alert("Champ incorrect");
		window.location.reload();
		alert("");
	}
	
	if (/^[a-z ,.'-]+$/i.test(firstnameVal)){
	}
	else{
		document.form.firstname.style.backgroundColor = "red";
		alert("Champ incorrect");
		window.location.reload();
		alert("");
	}
	
	if (/^[a-z0-9]+$/i.test(regisVal)){
	}
	else{
		document.form.registrationcgi.style.backgroundColor = "red";
		alert("Champ incorrect");
		window.location.reload();
		alert("");
	}
	
	if (/^[a-z0-9]+$/i.test(loginVal)){
	}
	else{
		document.form.login.style.backgroundColor = "red";
		alert("Champ incorrect");
		window.location.reload();
		alert("");
	}
	
	if (/^[a-z0-9]+$/i.test(passwordVal)){
	}
	else{
		document.form.password.style.backgroundColor = "red";
		alert("Champ incorrect");
		window.location.reload();
		alert("");
	}
	
	if(sexVal == 'M' || sexVal == 'F'){
	}
	
	else{
		document.form.sex.style.backgroundColor = "red";
		alert("Veuillez saisir le sexe entre 'M' ou 'F'");
		window.location.reload();
		alert("");
	}
	
	if(enableVal == 'true' || enableVal == 'false'){
	}
	
	else{
		document.form.enable.style.backgroundColor = "red";
		alert("Veuillez saisir l'enable entre 'true' ou 'false'");
		window.location.reload();
		alert("");
	}
	
	alert("Votre utilisateur a bien été créé");

}
    
