var export_excel_form;
var export_pdf_form;

function exportPdf(){
	var dhxWins2 = new dhtmlXWindows();
	dhxWins2.attachViewportTo("mainDiv");

	var export_pdf_window = dhxWins2.createWindow({
		id : "exportPdfWindow",
		left : 0,
		right : 0,
		width : 400,
		height : 150,
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
	export_pdf_form.loadStruct("xml/export/export_pdf_form.xml");
	export_pdf_form.attachEvent("onButtonClick", function(name){
		window[name]();
	});
}

function exportExcel() {
	var dhxWins2 = new dhtmlXWindows();
	dhxWins2.attachViewportTo("mainDiv");

	var export_excel_window = dhxWins2.createWindow({
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
	export_excel_form.loadStruct("xml/export/export_excel_form.xml");
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
		showResponseXmlAlert("Favor de ingresar un nombre de archivo valido");
	}
	alert("");
}

function getAssetGridPdf(){
	export_pdf_form.validate();
	var fileName = export_pdf_form.getItemValue("fileName");
	if(fileName != null && fileName != ""){
		export_grid.toExcel("myReport.do?method=getAssetGridPdf&fileName="+fileName);
	}else{
		showResponseXmlAlert("Favor de ingresar un nombre de archivo valido");
	}
}
