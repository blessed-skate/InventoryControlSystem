function saveForm(){
	insert_form.send("myAsset.do?method=insertAsset","post",function(loader, response){
		try{
			var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
			var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[3].childNodes[0].data;
			if(responseCode == 0){
				showResponseXmlAlert(responseMessage);
				clearInsertForm();
				updateDHTMLXComponents();
			}else{
				showResponseXmlAlertError(responseMessage);
			}
		}catch(err){
			showResponseXmlAlertError(err.message);
		}
	});
}

function reloadSelect(value){
	insert_form.reloadOptions("idSubclass", "myCatalog.do?method=getLedger&idLedger="+value);
	if(insert_form.getItemValue("idSubclass") != "-1")
		insert_form.setItemValue("tag", value+insert_form.getItemValue("idSubclass"));
	else
		insert_form.setItemValue("tag", value);
}

function fillTag(value){
	insert_form.setItemValue("tag", insert_form.getItemValue("idLedger")+value);
}

function validateForm(){
	insert_form.validate();
}

function cancelForm(){
	showConfirm("Cancelar","¿Desea cancelar la operación, se perdera la información capturada?", clearInsertForm);
}

function clearInsertForm(){
	insert_form.clear();
	insert_form.reloadOptions("idSubclass", "myCatalog.do?method=getLedger&idLedger=");
	insert_form.lock();
}


function newAsset(){
	insert_form.unlock();
	insert_form.clear();
	insert_form.setItemFocus("idLedger");
}

function searchAsset(){
	search_form.clear();
	search_form.lock();
	search_form.load("myAsset.do?method=getAssetByTag&tag="+search_toolbar.getValue("seacrh_item_input"), function(){
		var idLedger = search_form.getItemValue("idLedger");
		if(idLedger != null && idLedger != ""){
			showResponseXmlAlert("Se abrio el registro");
		}else{
			showResponseXmlAlertError("No se encontro informacion con los datos ingresados");
		}
	});
}

function enabledAssetForm(){
	var idLedger = search_form.getItemValue("idLedger");
	if(idLedger != null && idLedger != ""){
		search_form.unlock();
		search_form.setItemFocus("description");
	}else{
		showResponseXmlAlertError("No se encontro informacion con los datos ingresados");
	}
}

function updateAsset(){
	search_form.send("myAsset.do?method=updateAsset","post",function(loader, response){
		try{
			var responseCode = loader.xmlDoc.responseXML.childNodes[0].childNodes[1].childNodes[0].data;
			var responseMessage = loader.xmlDoc.responseXML.childNodes[0].childNodes[3].childNodes[0].data;
			if(responseCode == 0){
				search_form.clear();
				search_form.lock();
				updateDHTMLXComponents();
				showResponseXmlAlert(responseMessage);
			}else{
				showResponseXmlAlertError(responseMessage);
			}
		}catch(err){
			showResponseXmlAlertError(err.message);
		}
	});
}

//dhtmlxError.catchError("load", function(a, b, data) {
//	showResponseXmlAlertError('${loading_err}' + '\n' + '${reload_msg}');
//});