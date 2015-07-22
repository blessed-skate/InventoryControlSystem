function doCreateCatLedger(){	
	dhxWinsLedger = new dhtmlXWindows();
	dhxWinsLedger.attachViewportTo("mainDiv");

	cat_ledger_window = dhxWinsLedger.createWindow({
		id : "catLedgerWindow",
		left : 0,
		right : 0,
		width : 700,
		height : 400,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Catalogo de cuentas contables",
		header : true
	});
	cat_ledger_window.button('park').hide();
	cat_ledger_window.button('minmax').hide();
	cat_ledger_window.keepInViewport(true);

	cat_ledger_layout = cat_ledger_window.attachLayout({
	    pattern: "2U",
	    cells: [
	        {id: "a", text: "Nueva cuenta contable", header: false, width: 370},
	        {id: "b", text: "Cuentas contables", header: false}
	    ]
	});
	
	cat_ledger_form = cat_ledger_layout.cells("a").attachForm();
	cat_ledger_form.load("xml/cat_ledger_form.xml");
	cat_ledger_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	cat_ledger_form.lock();
	
	cat_ledger_toolbar = cat_ledger_layout.cells("a").attachToolbar({
		icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
		xml: "xml/cat_ledger_toolbar.xml" 
	});
	
	cat_ledger_grid = cat_ledger_layout.cells("b").attachGrid();
	cat_ledger_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	cat_ledger_grid.setHeader("&nbsp;, Nombre Usuario, Contraseña, Role, Enable, Nombre(s), Apellido(s), Sexo, Fecha Naciemiento, Fecha Registro, Fecha Ultima Actualizacion");
	cat_ledger_grid.attachHeader("#rspan,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter,#text_filter");
	cat_ledger_grid.setColTypes("sub_row_grid,ro,ro,ro,ro,ro,ro,ro,price,ro,ro");
	cat_ledger_grid.setInitWidths("30,80,100,100,120,80,100,80,80,*,*");
	cat_ledger_grid.setColSorting("na,int,str,str,str,str,str,str,str,str,str");
	cat_ledger_grid.setColAlign("left,left,left,left,left,letf,left,left,left,left,left");
	cat_ledger_grid.init();
	cat_ledger_grid.enableAutoHeight();
}

function doInsertFormSaveLedger(){
	showError("Insertando nuevo usuario...");
}

function doOnNewItemLedger(){
	cat_ledger_form.unlock();
	cat_ledger_form.clear();
	cat_ledger_form.setItemFocus("username");
}