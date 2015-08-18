var dhxWinsAbout, about_window, about_layout, aboutForm	;
function doCreateCatProperty(){	
	dhxWinsAbout = new dhtmlXWindows();
	dhxWinsAbout.attachViewportTo("mainDiv");

	about_window = dhxWinsAbout.createWindow({
		id : "aboutWindow",
		left : 0,
		right : 0,
		width : 1000,
		height : 600,
		center : true,
		move : false,
		modal : true,
		resize : false,
		caption : "Acerca...",
		header : true
	});
	about_window.button('park').hide();
	about_window.button('minmax').hide();
	about_window.keepInViewport(true);

	about_layout = cat_ledger_window.attachLayout({pattern: "1C", cells: [{id: "a", text: "Acerca...", header: false, width: 370}]});
	
	aboutForm = cat_property_layout.cells("a").attachForm();
	aboutForm.load("xml/aboutForm.xml");
	aboutForm.attachEvent("onButtonClick", function(name){
		window[name]();
	});
	aboutForm.lock();
}