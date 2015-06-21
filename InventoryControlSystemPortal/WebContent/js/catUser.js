function doCreateCatUserWindow(){
	dhxWinsUser = new dhtmlXWindows();
	dhxWinsUser.attachViewportTo("mainDiv");

	cat_user_window = dhxWinsUser.createWindow({
		id : "catUserWindow",
		left : 0,
		right : 0,
		width : 1000,
		height : 600,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Catalogo de usuarios",
		header : true
	});
	cat_user_window.button('park').hide();
	cat_user_window.button('minmax').hide();
	cat_user_window.keepInViewport(true);

	cat_user_layout = cat_user_window.attachLayout({
	    pattern: "2U",
	    cells: [
	        {id: "a", text: "Nuevo usuario", header: false, width: 370},
	        {id: "b", text: "Usuarios", header: true}
	    ]
	});
	
	cat_user_form = cat_user_layout.cells("a").attachForm();
	cat_user_form.load("xml/cat_user_form.xml");
	cat_user_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	cat_user_form.lock();
	
	cat_user_toolbar = cat_user_layout.cells("a").attachToolbar({
		icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
		xml: "xml/cat_user_toolbar.xml" 
	});
	
	cat_user_grid = cat_user_layout.cells("b").attachGrid();
	cat_user_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	cat_user_grid.setHeader("Nombre Usuario, Role, Nombre(s), Apellido(s), Sexo, Fecha Naciemiento, Fecha Registro, Fecha Ultima Actualizacion");
	cat_user_grid.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
	cat_user_grid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro");
	cat_user_grid.setInitWidths("80,100,100,120,100,100,80,80");
	cat_user_grid.setColSorting("str,str,str,str,str,str,str,str");
	cat_user_grid.setColAlign("left,left,left,left,letf,left,left,left");
	cat_user_grid.init();
	cat_user_grid.enableAutoHeight();
	cat_user_grid.loadXML("adminUser.do?method=getUsers");
}

function doInsertFormSaveUser(){
	cat_user_form.send("adminUser.do?method=insertUser","post",function(loader, response){
		try{
			var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[0].childNodes[0].data;
			var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
			if(responseCode == 0){
				showResponseXmlAlert(responseMessage);
				cat_user_form.clear();
				cat_user_grid.loadXML("adminUser.do?method=getUsers");
			}else{
				showResponseXmlAlertError(responseMessage);
			}
		}catch(err){
			showResponseXmlAlertError(err.message);
		}
	});
}

function doOnNewItemUser(){
	cat_user_form.unlock();
	cat_user_form.clear();
	cat_user_form.setItemFocus("username");
}