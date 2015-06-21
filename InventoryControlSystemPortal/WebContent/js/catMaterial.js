function doCreateCatMaterial(){	
	dhxWinsMaterial = new dhtmlXWindows();
	dhxWinsMaterial.attachViewportTo("mainDiv");

	cat_material_window = dhxWinsMaterial.createWindow({
		id : "catMaterialWindow",
		left : 0,
		right : 0,
		width : 1000,
		height : 600,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Catalogo de materiales",
		header : true
	});
	cat_material_window.button('park').hide();
	cat_material_window.button('minmax').hide();
	cat_material_window.keepInViewport(true);

	cat_material_layout = cat_material_window.attachLayout({
	    pattern: "2U",
	    cells: [
	        {id: "a", text: "Nuevo material", header: false, width: 370},
	        {id: "b", text: "Materiales", header: true}
	    ]
	});
	
	cat_material_form = cat_material_layout.cells("a").attachForm();
	cat_material_form.load("xml/cat_material_form.xml");
	cat_material_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	cat_material_form.lock();
	
	cat_material_toolbar = cat_material_layout.cells("a").attachToolbar({
		icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
		xml: "xml/cat_material_toolbar.xml" 
	});
	
	cat_material_grid = cat_material_layout.cells("b").attachGrid();
	cat_material_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	cat_material_grid.setHeader("&nbsp;, Nombre Usuario, Contraseña, Role, Enable, Nombre(s), Apellido(s), Sexo, Fecha Naciemiento, Fecha Registro, Fecha Ultima Actualizacion");
	cat_material_grid.attachHeader("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
	cat_material_grid.setColTypes("sub_row_grid,ro,ro,ro,ro,ro,ro,ro,price,ro,ro");
	cat_material_grid.setInitWidths("30,80,100,100,120,80,100,80,80,*,*");
	cat_material_grid.setColSorting("na,int,str,str,str,str,str,str,str,str,str");
	cat_material_grid.setColAlign("left,left,left,left,left,letf,left,left,left,left,left");
	cat_material_grid.init();
	cat_material_grid.enableAutoHeight();
}

function doInsertFormSaveMaterial(){
	showError("Insertando nuevo usuario...");
}

function doOnNewItemMaterial(){
	cat_material_form.unlock();
	cat_material_form.clear();
	cat_material_form.setItemFocus("username");
}