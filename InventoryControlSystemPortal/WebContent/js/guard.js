function updateGuardGrid(directlyResponsible){
	try{	
		guard_grid.clearAndLoad("myAsset.do?method=getDirectlyResponsibleAsset&directlyResponsible="+directlyResponsible, function(){});
	}catch(err){
		window.location.reload(true);
	}
}

function reloadDirectlyResponsibleSelect(){
	try{
		guard_form.reloadOptions("directlyResponsible", "myAsset.do?method=getDirectlyResponsible");
	}catch(e){
		window.location.reload(true);
	}
}

function getAssetGuardPdf(){
	var directlyResponsible = guard_form.getItemValue("directlyResponsible");
	if(directlyResponsible != null && directlyResponsible != "" && directlyResponsible != -1){
		if(guard_form.validate())
			replaceGuardAccent(guard_form);
			downloadFile("myReport.do?method=getGuardPdf", guard_form.getFormData());
	}else{
		showResponseXmlAlertError("Favor de seleccione un responsable");
	}
}

