<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tags-conf/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tags-conf/struts-logic.tld" prefix="logic"%>
<jsp:include page="header.jsp"></jsp:include>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Sistema de control de Inventarios</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

			
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
	position: relative;
	margin-top: 20px;
	margin-left: 20px;
	width: 1000px;
	height: 600px;
	display: block;
	margin-left: auto;
	margin-right: auto;
}

body{
	background-color: #deefff;
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
	
	function logOut(){
		alert("Salir");
		logout_form_hidden.submit();
	}
	
	function doOnLoad() {
		main_div = new dhtmlXLayoutObject("mainDiv", "1C");
		main_menu = main_div.cells("a").attachMenu({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/main_menu.xml"
		});
		main_menu.attachEvent("onClick", function(id, zoneId, cas){
			window[id]();
		});
		
		home_tabbar = main_div.cells("a").attachTabbar();
// 		home_tabbar = new dhtmlXTabBar("homeTabbar");
		
		home_tabbar.addTab("a1", "Alta", null, null, true);
		home_tabbar.addTab("a2", "Reportes");
		home_tabbar.addTab("a3", "Importar");
		
		//New item
		insert_layout = home_tabbar.tabs("a1").attachLayout({
		    pattern: "2U",
		    cells: [
		        {id: "a", text: "Nuevo Registro", header: false, collapse: false, fixSize: [true, true]}
		        ,{id: "b", text: "Buscar", header: false, width: 500, collapse: false, fixSize: [true, true]}
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
		        {id: "a", text: "Filtro", header: true, height: 180},
		        {id: "b", text: "Resultado", header: false}
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
		export_grid.loadXML("xml/export/query_sgrid.xml");
		export_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
// 		export_grid.setHeader("&nbsp;, C. contable, Tipo de bien, Etiqueta, Factura, Fecha, Localizacion, F. de uso, Valor, Ubicaion, Seguro");
		export_grid.attachHeader("#text_filter,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#rspan,#numeric_filter,#rspan,#rspan,#rspan,#rspan");
// 		export_grid.setColTypes("sub_row_grid,ro,ro,ro,ro,ro,ro,ro,price,ro,ro");
// 		export_grid.setInitWidths("30,80,100,100,120,80,100,80,80,*,*");
// 		export_grid.setColSorting("na,int,na,na,str,date,str,date,int,str,str");
// 		export_grid.setColAlign("left,left,center,center,left,letf,left,left,right,left,left");
 		export_grid.setNumberFormat("0,000.00",7,".",",");
 		export_grid.setDateFormat("%d/%m/%Y");
		export_grid.init();
		export_grid.enableAutoHeight();
		export_grid.attachFooter(" , , , , , , , , , , , , ,Total,#stat_total, , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , , ,Maximo,#stat_max, , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , , ,Minimo,#stat_min, , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , , ,Promedio,#stat_average, , , , ");
		export_grid.attachFooter(" , , , , , , , , , , , , ,Registros,#stat_count, , , , ");
		
		export_grid.loadXML("myAsset.do?method=getAsset");
		
		//Reports
		import_toolbar = home_tabbar.tabs("a3").attachToolbar({
			icon_path: "imgs/dhtmlx/dhtmlxToolbar/",
			xml: "xml/import/import_toolbar.xml"
		});
		
		import_grid = home_tabbar.tabs("a3").attachGrid();
		import_grid.loadXML("xml/import/import_grid.xml");
		import_grid.setImagePath("js/dhtmlx/skins/web/imgs/dhxgrid_web/");
	}
</script>
</head>
<body onload="doOnLoad();">
	<div id="mainDiv"></div>
	<form action="j_acegi_logout" method="post"
		name="logout_form_hidden">
	</form>
</body>
</html>