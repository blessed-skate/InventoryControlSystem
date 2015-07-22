function doCreateCatProperty(){	
	dhxWinsProperty = new dhtmlXWindows();
	dhxWinsProperty.attachViewportTo("mainDiv");

	cat_property_window = dhxWinsProperty.createWindow({
		id : "catPropertyWindow",
		left : 0,
		right : 0,
		width : 1000,
		height : 600,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Catalogo de propiedades",
		header : true
	});
	cat_property_window.button('park').hide();
	cat_property_window.button('minmax').hide();
	cat_property_window.keepInViewport(true);

	cat_property_layout = cat_property_window.attachLayout({
	    pattern: "2U",
	    cells: [
	        {id: "a", text: "Nueva propiedad", header: false, width: 370},
	        {id: "b", text: "Propiedades", header: true}
	    ]
	});
	
	cat_property_form = cat_property_layout.cells("a").attachForm();
	cat_property_form.load("xml/cat_property_form.xml");
	cat_property_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	cat_property_form.lock();
	
	cat_property_toolbar = cat_property_layout.cells("a").attachToolbar({
		icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
		xml: "xml/cat_property_toolbar.xml" 
	});
	
	cat_property_grid = cat_property_layout.cells("b").attachGrid();
	cat_property_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	cat_property_grid.setHeader("Id Propiedad,Valor");
	cat_property_grid.attachHeader("#text_filter,#text_filter");
	cat_property_grid.setColTypes("ro,ro");
	cat_property_grid.setInitWidths("150,150");
	cat_property_grid.setColSorting("str,str");
	cat_property_grid.setColAlign("left,left");
	cat_property_grid.init();
	cat_property_grid.enableAutoHeight();
	cat_property_grid.loadXML("adminProperty.do?method=getPropertys");
}

function doInsertFormSaveProperty(){
	cat_property_form.send("adminProperty.do?method=insertProperty","post",function(loader, response){
		try{
			var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[0].childNodes[0].data;
			var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
			if(responseCode == 0){
				showResponseXmlAlert(responseMessage);
				cat_property_form.clear();
				cat_property_grid.loadXML("adminProperty.do?method=getPropertys");
			}else{
				showResponseXmlAlertError(responseMessage);
			}
		}catch(err){
			showResponseXmlAlertError(err.message);
		}
	});
}

function doOnNewItemProperty(){
	cat_property_form.unlock();
	cat_property_form.clear();
	cat_property_form.setItemFocus("idProperty");
}