//dhtmlxError.catchError("LoadXML", function(a, b, data) {
//	showError('${loading_err}' + '\n' + '${reload_msg}');
//});
dhx4.ajax.cache=true;

//function doOnLoad(){
//	showResponseXmlAlertWarning("doOnLoad");
//}

function showResponseXmlAlertError(msg) {
	dhtmlx.alert({
		title : "Error",
		type : "alert-error",
		text : msg
	});
}

function showResponseXmlAlertWarning(msg) {
	dhtmlx.alert({
		title : "Warning",
		type : "alert-warning",
		text : msg
	});
}

function showResponseXmlAlert(msg) {
	dhtmlx.alert({
		title : "Alerta",
		type : "alert",
		text : msg
	});
}

function showConfirmWarning(title, question) {
	dhtmlx.confirm({
		title : title,
		type : "confirm-warning",
		text : question,
		callback : function(result) {
			dhtmlx.message("Result: " + result)
		}
	});
}

function showConfirmError(title, question) {
	dhtmlx.confirm({
		title : title,
		type : "confirm-error",
		text : question,
		callback : function(result) {
			dhtmlx.message("Result: " + result)
		}
	});
}

function showConfirm(title, question, execute) {
	dhtmlx.confirm({
		title : title,
		type : "confirm",
		text : question,
		callback : function(result) {
			if(result)	
				execute();
		}
	});
}

function showError(msg) {
	dhtmlx.alert({
		title : "Error",
		type : "alert-error",
		text : msg
	});
}

function updateDHTMLXComponents(){
	reloadDirectlyResponsibleSelect();
	export_grid.clearAndLoad("myAsset.do?method=getAsset",function(){
//		showResponseXmlAlert("La tabla se ha refrescado...");
	});
}
