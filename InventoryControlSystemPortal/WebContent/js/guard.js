function updateGuardGrid(directlyResponsible){
//	var directlyResponsible = guard_form.getSelect("directlyResponsible").value;
//	alert(directlyResponsible);
	try{	
		guard_grid.clearAndLoad("myAsset.do?method=getDirectlyResponsibleAsset&directlyResponsible="+directlyResponsible, function(){
//			showResponseXmlAlert("La tabla se ha refrescado...");
		});
		
	}catch(err){
		showResponseXmlAlertError(err.message);
	}
}

function reloadDirectlyResponsibleSelect(){
	guard_form.reloadOptions("directlyResponsible", "myAsset.do?method=getDirectlyResponsible");
}