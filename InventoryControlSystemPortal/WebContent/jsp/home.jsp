<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tags-conf/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tags-conf/struts-logic.tld" prefix="logic"%>
<%@taglib uri="http://acegisecurity.org/authz" prefix="authz"%>
<jsp:include page="header.jsp"></jsp:include>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
			
<style>
div.gridbox div.ftr td {
	text-align: right;
	background-color: #E5F2F8;
	border-right: 0 solid gray;
}

div.gridbox_light table.hdr td {
	text-align: center;
}

div#mainDiv {
/* 	position: relative; */
	margin-top: 20px;
	margin-left: 20px;
	width: 1000px;
	height: 600px;
/* 	display: block; */
	margin-left: auto;
	margin-right: auto;
}

body{
	background-color: #999999;
}

</style>
<script>
	var main_div;
	var main_menu;
	var home_tabbar;
	var export_grid;
	var insert_layout;
	var insert_form;
	var insert_toolbar;
	var search_form;
	var search_toolbar;
	var query_layout;
	var query_form;
	var import_grid;
	var import_toolbar;
	var guard_toolbar;
	var guard_layout;
	var guard_form;
	var guard_grid;
	var dhxWin;
	
	// Catalogos...
	var dhxWinsUser, cat_user_window, cat_user_layout, cat_user_form, cat_user_grid, cat_user_toolbar;
	var dhxWinsLedger, cat_ledger_window, cat_ledger_layout, cat_ledger_form, cat_ledger_grid, cat_ledger_toolbar;
	var dhxWinsProperty, cat_property_window, cat_property_layout, cat_property_form, cat_property_grid, cat_property_toolbar;
	var dhxWinsRole, cat_role_window, cat_role_layout, cat_role_form, cat_role_grid, cat_role_toolbar;
	function logOut(){
		logout_form_hidden.submit();
	}
	
	function doOnLoad() {
		dhxWin = new dhtmlXWindows();
		dhxWin.attachViewportTo("mainDiv");
		
		main_div = new dhtmlXLayoutObject("mainDiv", "1C");
		main_menu = main_div.cells("a").attachMenu({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/main_menu.xml"
		});
		
		main_menu.attachEvent("onClick", function(id, zoneId, cas){
			switch (id){
			case "user":
				catUsers();
				break;
			case "ledger":
				catLedger();
				break;
			case "property":
				catProperty();
				break;
			case "role":
				catRole();
				break;
			case "logOut":
				logOut();
				break;
			default:
				showError("ERROR...");
			}
		});
		
		home_tabbar = main_div.cells("a").attachTabbar();
		
		home_tabbar.addTab("a1", "Alta", null, null, true);
		home_tabbar.addTab("a2", "Reportes");
		home_tabbar.addTab("a3", "Importar");
		home_tabbar.addTab("a4", "Resguardo");
		
		//New item
		insert_layout = home_tabbar.tabs("a1").attachLayout({
		    pattern: "2U",
		    cells: [
		        {id: "a", text: "Nuevo Registro", header: false, collapse: false, fixSize: [1, 1]}
		        ,{id: "b", text: "Buscar", header: false, width: 500, collapse: false, fixSize: [1, 1]}
// 		        ,{id: "c", text: "Lista", collapse: false, fixSize: [true, true]}
		    ]
		});
		
		insert_form = insert_layout.cells("a").attachForm();
		insert_form.load("xml/insert/insert_form.xml");
		insert_form.enableLiveValidation(true);
		insert_form.attachEvent("onButtonClick", function(name){
			window[name]();
		});
		insert_form.attachEvent("onInputChange", function(name, value, form){
			if(name == "idLedger"){
		    	reloadSelect(value);
		    }else if(name == "idSubclass"){
		    	fillTag(value);
		    }
		});
		insert_form.lock();
		
		insert_toolbar = insert_layout.cells("a").attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/insert/insert_toolbar.xml" 
		});
		
		insert_toolbar.attachEvent("onClick", function(name){
			window[name]();
		});
		
		search_form = insert_layout.cells("b").attachForm();
		search_form.load("xml/insert/search_form.xml");
		search_form.enableLiveValidation(true);
		search_form.attachEvent("onButtonClick", function(name){
			window[name]();
		});
		search_form.lock();
		
		search_toolbar = insert_layout.cells("b").attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/insert/search_toolbar.xml" 
		});
		
		search_toolbar.attachEvent("onClick", function(name){
			window[name]();
		});
		
		//Filters
		query_layout = home_tabbar.tabs("a2").attachLayout({
		    pattern: "2E",
		    cells: [
		        {id: "a", text: "Filtro", header: true, fixSize: [1, 1], height: 180},
		        {id: "b", text: "Resultado", header: false, fixSize: [1, 1]}
		    ]
		});
		
		query_layout.cells("a").collapse();
		query_form = query_layout.cells("a").attachForm();
		query_form.load("xml/export/query_form.xml");
		query_form.attachEvent("onButtonClick", function(name){
			window[name]();
		});
		
		
		query_layout.cells("b").attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/export/export_toolbar.xml"
		});
		
		export_grid = query_layout.cells("b").attachGrid();
		export_grid.loadXML("xml/grid.xml");
		export_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
		export_grid.attachHeader("#text_filter,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#numeric_filter,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan");
 		export_grid.setNumberFormat("0, 000.00",14,".",",");
 		export_grid.setDateFormat("%d/%m/%Y");
 		export_grid.init();
		export_grid.enableAutoHeight();
		export_grid.attachFooter(" , , , , , , , , , , , ,Total,#stat_total, , , , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , ,Maximo,#stat_max, , , , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , ,Minimo,#stat_min, , , , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , ,Promedio,#stat_average, , , , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , ,Registros,#stat_count, , , , , , ");
		
// 		export_grid.loadXML("myAsset.do?method=getAsset");
		export_grid.enableSmartRendering(true);
		
		//Reports
		import_toolbar = home_tabbar.tabs("a3").attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/import/import_toolbar.xml"
		});
		
		import_grid = home_tabbar.tabs("a3").attachGrid();
		import_grid.enableSmartRendering(true);
		import_grid.loadXML("xml/grid.xml");
		import_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
		import_grid.setNumberFormat("0, 000.00",14,".",",");
		import_grid.setDateFormat("%d/%m/%Y");
		import_grid.init();
		import_grid.enableAutoHeight();
		
		
		//Resguardos
		guard_layout = home_tabbar.tabs("a4").attachLayout({
		    pattern: "2E",
		    cells: [
		        {id: "a", text: "Responsable directo", collapse: false, fixSize: [1, 1], height: 200}
		        ,{id: "b", text: "Activos", collapse: false, fixSize: [1, 1]}
		    ]
		});
		
		guard_form = guard_layout.cells("a").attachForm();
		guard_form.load("xml/guard/guard_form.xml");
		guard_form.attachEvent("onButtonClick", function(name){
			window[name]();
		});
		
		guard_form.attachEvent("onInputChange", function(name, value, form){
			if(name == "directlyResponsible"){
				updateGuardGrid(value);
		    }
		});
		
		guard_grid = guard_layout.cells("b").attachGrid();
		guard_grid.enableSmartRendering(true);
		guard_grid.loadXML("xml/guard/guard_grid.xml");
		guard_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
 		guard_grid.setNumberFormat("$ 0,000.00",7,".",",");
 		guard_grid.setDateFormat("%d/%m/%Y");
		guard_grid.init();
		guard_grid.enableAutoHeight();
		guard_grid.attachFooter(" , , , , ,Total,#stat_total");
		guard_grid.attachFooter(" , , , , ,Maximo,#stat_max");
		guard_grid.attachFooter(" , , , , ,Minimo,#stat_min");
		guard_grid.attachFooter(" , , , , ,Promedio,#stat_average");
		guard_grid.attachFooter(" , , , , ,Registros,#stat_count");
		
// 		updateDHTMLXComponents();
		defineRole();
	}
		
</script>
<authz:authorize ifNotGranted="ROLE_ADMIN, ROLE_USER" ifAllGranted="ROLE_QUERY">
	<script type="text/javascript">
		function defineRole(){
			home_tabbar.tabs("a1").disable("a2");
			home_tabbar.tabs("a3").disable();
			main_menu.removeItem("catalogs");
		}
	</script>
</authz:authorize>
<authz:authorize ifNotGranted="ROLE_ADMIN, ROLE_QUERY" ifAllGranted="ROLE_USER" >
	<script type="text/javascript">
		function defineRole(){
			main_menu.removeItem("catalogs");
		}
	</script>
</authz:authorize>

</head>
<body onload="doOnLoad();">
	<div id="mainDiv"></div>
	<form action="j_acegi_logout" method="post"
		name="logout_form_hidden">
	</form>
	
</body>
</html>