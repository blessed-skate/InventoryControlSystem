var dhwin, import_excel_window, id="import_excel_window";
var myVault, mygrid;
var fileToGrid;

function importExcel(){
	try{
		dhwin = new dhtmlXWindows();
		import_excel_window = dhwin.createWindow({
			id : "exportExcelWindow",
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
				report_grid.loadXMLString(extra.param);
				import_toolbar.setValue("file", file.name);
			}else{
				showResponseXmlAlertError("<b>Ocurrio un error cargar el archvio</b>: "+file.name + " " +extra.info)
			}
		});
		
		myVault.attachEvent("onUploadFail", function(file, extra){
			showResponseXmlAlertError("<b>Ocurrio un error cargar el archvio</b>: "+file.name + " " +extra.info)			
		});
		
		var import_excel_toolbar = import_excel_window.attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/import/import_excel_toolbar.xml"
		});
		
		import_excel_toolbar.attachEvent("onClick", function(name){
			window[name]();
		});

	}catch(e){
		alert("Error: "+ e.toString());
	}
}

function saveFileImported(){
	var textFileValue = import_toolbar.getValue("file");
	if(textFileValue != null && textFileValue != ""){
		var loader = window.dhx4.ajax.post("myImport.do", "method=saveFile&fileName="+textFileValue);
		
		console.log(loader.xmlDoc.responseXML);
		alert(loader.xmlDoc.responseXML);
		
		var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[0].childNodes[0].data;
		var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
		if(responseCode == 0){
			showResponseXmlAlert(responseMessage);
			insert_form.clear();
		}else{
			showResponseXmlAlertError(responseMessage);
		}
	}else{
		showResponseXmlAlertError("Por favor importe algun archivo");
	} 
}


function setExcelHeader(){
	alert(import_excel_toolbar.getItemState("header"));
}