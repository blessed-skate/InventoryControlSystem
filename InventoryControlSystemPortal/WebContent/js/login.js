var login_form;

function doOnLoad() {
	dhxWin = new dhtmlXWindows();
	var login_window = dhxWin.createWindow("loginWindow", 0, 100, 400, 280);
	login_window.setText("Sistema de Control de Inventarios");
	// login_window.setModal(true);
	login_window.button('park').hide();
	login_window.button('minmax').hide();
	login_window.button('close').hide();
	login_window.centerOnScreen();

	login_form = login_window.attachForm();
	login_form.loadStruct("xml/login_form.xml");
	login_form.setItemFocus("username");

	login_form.attachEvent("onButtonClick", function(id) {
		if (id == "login") {
			sendLoginForm();
		}
	});

	login_form.attachEvent("onKeyup", function(inp, ev, name, value) {
		if (ev.keyCode == 13 && name == "password") {
			sendLoginForm();
		}

		// if (id == "login") {
		// document.getElementById("j_username").value = login_form
		// .getItemValue("username");
		// document.getElementById("j_password").value = login_form
		// .getItemValue("password");
		// alert("Set " + document.getElementById("j_username").value
		// + document.getElementById("j_password").value);
		// login_form_hidden.submit();
		// }
	});
}

function sendLoginForm() {
	document.getElementById("j_username").value = login_form
			.getItemValue("username");
	document.getElementById("j_password").value = login_form
			.getItemValue("password");
	login_form_hidden.submit();
}
