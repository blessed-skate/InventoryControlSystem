var import_excel_window; 
var myVault, mygrid;
var fileToGrid;

function importExcel(){
	try{
		import_grid.clearAll();
		import_excel_window = dhxWin.createWindow({
			id : "importExcelWindow",
			left : 0,
			right : 0,
			width : 500,
			height : 400,
			center : true,
			move : false,
			modal : true,
			resize : false,
			caption : "Importar Excel",
			header : true
		});
		
		import_excel_window.button("park").hide();
		import_excel_window.button("minmax").hide();
		
		myVault = import_excel_window.attachVault({
			uploadUrl:  "myImport.do?method=importExcel&header=true&sheet=0",
			filesLimit: 1,
			multiple: false,
			buttonClear: false
		});
		
		myVault.attachEvent("onUploadFile", function(file, extra){
			if(file.uploaded){
				showResponseXmlAlert("<b>"+extra.info+"</b>: " + file.name);
				import_grid.loadXMLString(extra.param);
				import_toolbar.setValue("file", file.name);
				updateDHTMLXComponents();
				dhxWin.window("importExcelWindow").close();
			}else{
				showResponseXmlAlertError("<b>Ocurrio un error cargar el archivo</b>: "+file.name + " " + (extra != null ? extra.info : ""));
				updateDHTMLXComponents();
				dhxWin.window("importExcelWindow").close();
			}
		});
		
		myVault.attachEvent("onUploadFail", function(file, extra){
			showResponseXmlAlertError("<b>Ocurrio un error cargar el archivo</b>: "+file.name + " " + (extra != null ? extra.info : ""));
			updateDHTMLXComponents();
			dhxWin.window("importExcelWindow").close();
		});
		
		myVault.attachEvent("onBeforeFileAdd", function(file){
			var ext = this.getFileExtension(file.name);
			if(ext == "xls" || ext == "xlsx"){
				return true;
			}else{
				showResponseXmlAlertError("<b>La extensión del archivo es inválida</b>: " + ext)
				return false;
			}
		});
		
		var import_excel_toolbar = import_excel_window.attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/import/import_excel_toolbar.xml"
		});
		
		import_excel_toolbar.attachEvent("onClick", function(name){
			showResponseXmlAlert(name);
		});
		
		import_excel_toolbar.attachEvent("onStateChange", function(id, state){
			var sheet = import_excel_toolbar.getValue("sheet_item_input");
			myVault.setURL("myImport.do?method=importExcel&header="+state+"&sheet="+sheet);
			showResponseXmlAlert(myVault.getURL());
		});

	}catch(err){
		showResponseXmlAlertError(err.message);
		dhxWin.window("importExcelWindow").close();
	}
}

function setExcelHeader(){
	alert(import_excel_toolbar.getItemState("header"));
}