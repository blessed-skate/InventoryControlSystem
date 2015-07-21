dhx4.ajax.cache=true;

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

var downloadFrame;
function downloadFile(url, params){
	if(downloadFrame == null){
		downloadFrame = document.createElement("iframe");
		downloadFrame.className = "download_iframe";
		downloadFrame.name = "download_frame";
		downloadFrame.border = downloadFrame.frameBorder = 0;
		document.body.appendChild(downloadFrame);		
	}
	
	var downloadForm = document.createElement("FORM");
	downloadForm.action = url;
	downloadForm.method = "POST";
	downloadForm.target = "download_frame";
	document.body.appendChild(downloadForm);
	
	// add params to form
	for (var a in params) {
		var input = document.createElement("INPUT");
		input.type = "hidden";
		input.name = a;
		input.value = params[a];
		downloadForm.appendChild(input);
		input = null;
	}
	
	// submit form
	downloadForm.submit();
	
	// clear form
	window.setTimeout(function(){
		document.body.removeChild(downloadForm);
		downloadForm = null;
	}, 1);
}