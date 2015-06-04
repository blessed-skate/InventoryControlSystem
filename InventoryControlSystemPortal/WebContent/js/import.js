var dhwin, ulwindow, ulLayout, id="ulwindow";
var myVault, mygrid;
var fileToGrid;

//function onLoad(){
//	createWindow();
//}

function importExcel(){
	try{
		dhwin = new dhtmlXWindows();
		ulwindow = dhwin.createWindow({
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
		
		ulwindow.button('park').hide();
		ulwindow.button('minmax').hide();

		myVault = ulwindow.attachVault({
			uploadUrl:  "myImport.do?method=importExcel"      // html4/html5 upload url
		});
		
		myVault.attachEvent("onFileAdd", function(file){
//			alert(file.serverName);
//			alert(file.uploaded);
		});
		
		myVault.attachEvent("onUploadComplete", function(files){
//			showResponseXmlAlert(files[0].size + " " + files[0].serverName);
//			showResponseXmlAlert(files[0].uploaded);
		});
		
		myVault.attachEvent("onUploadFile", function(file, extra){
			if(file.uploaded){
				showResponseXmlAlert("<b>"+extra.info+"</b>: " + file.name);
				report_grid.loadXMLString(extra.param);
			}else{
				showResponseXmlAlertError("<b>Ocurrio un error cargar el archvio</b>: "+file.name + " " +extra.info)
			}
		});

	}catch(e){
		alert("Error: "+ e.toString());
	}
}


