function doCreateCatRole(){	
	dhxWinsRole = new dhtmlXWindows();
	dhxWinsRole.attachViewportTo("mainDiv");

	cat_role_window = dhxWinsRole.createWindow({
		id : "catRoleWindow",
		left : 0,
		right : 0,
		width : 1000,
		height : 600,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Catalogo de roles",
		header : true
	});
	cat_role_window.button('park').hide();
	cat_role_window.button('minmax').hide();
	cat_role_window.keepInViewport(true);

	cat_role_layout = cat_role_window.attachLayout({
	    pattern: "2U",
	    cells: [
	        {id: "a", text: "Nuevo rol", header: false, width: 370},
	        {id: "b", text: "Roles", header: true}
	    ]
	});
	
	cat_role_form = cat_role_layout.cells("a").attachForm();
	cat_role_form.load("xml/cat_role_form.xml");
	cat_role_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	cat_role_form.lock();
	
	cat_role_toolbar = cat_role_layout.cells("a").attachToolbar({
		icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
		xml: "xml/cat_role_toolbar.xml" 
	});
	
	cat_role_grid = cat_role_layout.cells("b").attachGrid();
	cat_role_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	cat_role_grid.setHeader("Id Rol, Rol, Descripcion, Fecha Registro, Fecha Ultima Actualizacion");
	cat_role_grid.attachHeader("#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
	cat_role_grid.setColTypes("ro,ro,ro,ro,ro");
	cat_role_grid.setInitWidths("80,100,100,120,100");
	cat_role_grid.setColSorting("str,str,str,str,str");
	cat_role_grid.setColAlign("left,left,left,left,letf");
	cat_role_grid.init();
	cat_role_grid.enableAutoHeight();
	cat_role_grid.loadXML("adminRole.do?method=getRoles");
}

function doInsertFormSaveRole(){
	cat_role_form.send("adminRole.do?method=insertRole","post",function(loader, response){
		try{
			var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[0].childNodes[0].data;
			var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
			if(responseCode == 0){
				showResponseXmlAlert(responseMessage);
				cat_role_form.clear();
				cat_role_grid.loadXML("adminRole.do?method=getRoles");
			}else{
				showResponseXmlAlertError(responseMessage);
			}
		}catch(err){
			showResponseXmlAlertError(err.message);
		}
	});
}

function doOnNewItemRole(){
	cat_role_form.unlock();
	cat_role_form.clear();
	cat_role_form.setItemFocus("valueRole");
}