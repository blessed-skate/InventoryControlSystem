var login_form;

function doOnLoad() {
	dhxWin = new dhtmlXWindows();
	var login_window = dhxWin.createWindow("loginWindow", 0, 0, 740, 420);
	var loginLayout = login_window.attachLayout("2E");
	
	var cell_1 = loginLayout.cells("a");
	cell_1.setHeight("240");
//	cell_1.setWidth("725");
	cell_1.hideHeader();
	cell_1.fixSize(1,1);
	cell_1.attachURL("imgs/logo2.png");
	
	var cell_2 = loginLayout.cells("b");
	cell_2.hideHeader();
	cell_2.fixSize(1,1);
	
	login_form = cell_2.attachForm();
	login_form.loadStruct("xml/login_form.xml");
	login_form.setItemFocus("username");
	login_form.setFontSize("15px");

	login_form.attachEvent("onButtonClick", function(id) {
		if (id == "login") {
			sendLoginForm();
		}
	});
	
	login_form.attachEvent("onKeyup", function(inp, ev, name, value) {
		if (ev.keyCode == 13 && name == "password") {
			sendLoginForm();
		}
	});
	
	login_window.denyResize();
	login_window.denyMove();
//	login_window.setModal(1);
	login_window.hideHeader();
	login_window.centerOnScreen();
	
//	login_window.button('park').hide();
//	login_window.button('minmax').hide();
//	login_window.button('close').hide();
//	login_window.setText("Sistema de Control de Inventarios");
}

function sendLoginForm() {
	document.getElementById("j_username").value = login_form
			.getItemValue("username");
	document.getElementById("j_password").value = login_form
			.getItemValue("password");
	login_form_hidden.submit();
}
