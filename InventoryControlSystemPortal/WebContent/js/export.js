var export_excel_form;
var export_pdf_form;
var export_db_form;

function exportPdf(){
	var export_pdf_window = dhxWin.createWindow({
		id : "exportPdfWindow",
		left : 0,
		right : 0,
		width : 400,
		height : 130,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Exportar Pdf",
		header : true
	});
	export_pdf_window.button('park').hide();
	export_pdf_window.button('minmax').hide();
	export_pdf_window.keepInViewport(true);

	export_pdf_form = export_pdf_window.attachForm();
	export_pdf_form.loadStruct("xml/export/export_pdf_form.xml", function(){
		var d = new Date();
		var fileName = "REPORTE_PDF_"+d.toLocaleString();
		export_pdf_form.setItemValue("fileName", fileName);
	});
	export_pdf_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	export_pdf_form.setUserData("fileName", "value", "Hola");
}

function getAssetGridPdf(){
	export_pdf_form.validate();
	var fileName = export_pdf_form.getItemValue("fileName");
	if(fileName != null && fileName != ""){
		export_grid.toExcel("myReport.do?method=getAssetGridPdf&fileName="+fileName);
	}else{
		showResponseXmlAlertError("Favor de ingresar un nombre de archivo valido");
	}
}

function exportExcel() {

	var export_excel_window = dhxWin.createWindow(
		{
		id : "exportExcelWindow",
		left : 0,
		right : 0,
		width : 400,
		height : 200,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Exportar Excel",
		header : true
	});
	export_excel_window.button('park').hide();
	export_excel_window.button('minmax').hide();
	export_excel_window.keepInViewport(true);
	export_excel_form = export_excel_window.attachForm();
	export_excel_form.loadStruct("xml/export/export_excel_form.xml", function(){
		var d = new Date();
		var fileName = "REPORTE_EXCEL_"+d.toLocaleString();
		export_excel_form.setItemValue("fileName", fileName);
	});
	export_excel_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
}

function getAssetGridExcel(){
	export_excel_form.validate();
	var fileName = export_excel_form.getItemValue("fileName");
	if(fileName != null && fileName != ""){
		var header = export_excel_form.getItemValue("header");
		var extension = export_excel_form.getItemValue("extension");
		export_grid.toExcel("myReport.do?method=getAssetGridExcel&header="+header+"&fileName="+fileName+"&extension="+extension);
	}else{
		showResponseXmlAlertError("Favor de ingresar un nombre de archivo valido");
	}
}

function exportDb(){
	var dhxWins2 = new dhtmlXWindows();
	dhxWins2.attachViewportTo("mainDiv");

	var export_db_window = dhxWins2.createWindow({
		id : "exportDbWindow",
		left : 0,
		right : 0,
		width : 400,
		height : 150,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Exportar Base de Datos",
		header : true
	});
	export_db_window.button('park').hide();
	export_db_window.button('minmax').hide();
	export_db_window.keepInViewport(true);

	export_db_form = export_db_window.attachForm();
	export_db_form.loadStruct("xml/export/export_db_form.xml", function(){
		var d = new Date();
		var fileName = "RESPALDO_BD_"+d.toLocaleString();
		export_db_form.setItemValue("fileName", fileName);
	});
	export_db_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
}

function getAssetDbExcel(){
	export_db_form.validate();
	var fileName = export_db_form.getItemValue("fileName");
	if(fileName != null && fileName != ""){
		var extension = export_db_form.getItemValue("extension");
		export_grid.toExcel("myReport.do?method=getAssetDbExcel&fileName="+fileName+"&extension="+extension);
	}else{
		showResponseXmlAlertError("Favor de ingresar un nombre de archivo valido");
	}
}

function refreshExportGrid(){
	export_grid.clearAndLoad("myAsset.do?method=getAsset",function(){
		showResponseXmlAlert("La tabla se ha refrescado...");
	});
}