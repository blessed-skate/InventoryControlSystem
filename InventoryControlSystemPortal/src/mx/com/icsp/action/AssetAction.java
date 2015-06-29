package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.AssetResponse;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsc.common.util.XmlFactory;
import mx.com.icsp.service.AssetService;
import mx.com.icsp.service.PropertyServiceImpl;
import mx.com.icsp.util.Constants;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class AssetAction extends DispatchAction {

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	AssetService assetService;
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}

	public String dummyXML(Asset asset) {
		StringBuilder sb = new StringBuilder();
		sb.append("<rows>");
		sb.append("<header>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Descripcion</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Marca</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Modelo</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Serie</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Material</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Color</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Proveedor</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Reponsable general</column>");
		sb.append("<column type=\"dyn\" color=\"#d5f1ff\" sort=\"str\">Responsable directo</column>");
		sb.append("</header>");
		sb.append("<row>");
		sb.append("<cell>").append(asset.getDescription()).append("</cell>");
		sb.append("<cell>").append(asset.getBrand()).append("</cell>");
		sb.append("<cell>").append(asset.getModel()).append("</cell>");
		sb.append("<cell>").append(asset.getSerialNumber()).append("</cell>");
		
		sb.append("</row>");
		sb.append("</rows>");
		return sb.toString();
	}

	public void getAsset(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {

		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		StringBuilder sb = new StringBuilder();
		AssetResponse assetResponse = new AssetResponse();
		
		try {
			Asset[] assetArray = assetService.getAsset(idTransaction);
			
			if (assetArray != null) {
				sb.append("<rows>");
				for (Asset asset : assetArray) {
					sb.append("<row id=\"" + asset.getId() + "\">");
//					sb.append("<cell type=\"sub_row_grid\">").append("xml/query_sgrid.xml").append("</cell>");
					sb.append("<cell>").append(asset.getTag()).append("</cell>");
					sb.append("<cell>").append(asset.getSubclass()).append("</cell>");
					sb.append("<cell>").append(asset.getDescription()).append("</cell>");
					sb.append("<cell>").append(asset.getBrand()).append("</cell>");
					sb.append("<cell>").append(asset.getModel()).append("</cell>");
					sb.append("<cell>").append(asset.getSerialNumber()).append("</cell>");
					sb.append("<cell>").append(asset.getMaterial()).append("</cell>");
					sb.append("<cell>").append(asset.getColor()).append("</cell>");
					sb.append("<cell>").append(asset.getSupplier()).append("</cell>");
					sb.append("<cell>").append(asset.getDirectlyResponsible()).append("</cell>");
					sb.append("<cell>").append(asset.getGeneralManager()).append("</cell>");
					sb.append("<cell>").append(asset.getTag()).append("</cell>");
					sb.append("<cell>").append(asset.getBill()).append("</cell>");
					sb.append("<cell>").append(sdf.format(asset.getBillingDate())).append("</cell>");
					sb.append("<cell>").append(asset.getPrice()).append("</cell>");
					sb.append("<cell>").append(sdf.format(asset.getUseDate())).append("</cell>");
					sb.append("<cell>").append(asset.getLocation()).append("</cell>");
					sb.append("<cell>").append(asset.getGeneralLocation()).append("</cell>");
					sb.append("<cell>").append(asset.getSecure()).append("</cell>");
					sb.append("</row>");
				}
				sb.append("</rows>");
			} else {
				assetResponse.setResponseCode(100);
				assetResponse.setResponseMessage("No se encontraron registros");
				sb.append(XmlFactory.getXml(idTransaction, assetResponse));
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(101);
			assetResponse.setResponseMessage("Error al obtener registro");
			sb.append(XmlFactory.getXml(idTransaction, assetResponse));
		}

		setResponse(request, response, sb.toString(), "application/xml");
	}
	
	public void getAssetByTag(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {

		AssetResponse assetResponse = new AssetResponse();
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		StringBuilder sb = new StringBuilder();
		
		try {
			long tag = gerParameterLong(request, "tag");
			log.info(logPattern.buildPattern(methodName, idTransaction, "tag", String.valueOf(tag)));
			Asset asset = assetService.getAssetByTag(idTransaction, tag);
			log.info(logPattern.buildPattern(methodName, idTransaction, "asset", ToStringBuilder.reflectionToString(asset)));
			
			if (asset != null) {
				sb.append("<data>");
				sb.append("<idLedger>").append(asset.getIdLedger()).append("</idLedger>");
				sb.append("<idSubclass>").append(asset.getSubclass()).append("</idSubclass>");
				sb.append("<description>").append(asset.getDescription()).append("</description>");
				sb.append("<brand>").append(asset.getBrand()).append("</brand>");
				sb.append("<model>").append(asset.getModel()).append("</model>");
				sb.append("<serialNumber>").append(asset.getSerialNumber()).append("</serialNumber>");
				sb.append("<material>").append(asset.getMaterial()).append("</material>");
				sb.append("<color>").append(asset.getColor()).append("</color>");
				sb.append("<supplier>").append(asset.getSupplier()).append("</supplier>");
				sb.append("<generalManager>").append(asset.getGeneralManager()).append("</generalManager>");
				sb.append("<directlyResponsible>").append(asset.getDirectlyResponsible()).append("</directlyResponsible>");
				sb.append("<tag>").append(asset.getTag()).append("</tag>");
				sb.append("<bill>").append(asset.getBill()).append("</bill>");
				sb.append("<billingDate>").append(sdf.format(asset.getBillingDate())).append("</billingDate>");
				sb.append("<location>").append(asset.getLocation()).append("</location>");
				sb.append("<useDate>").append(sdf.format(asset.getUseDate())).append("</useDate>");
				sb.append("<price>").append(asset.getPrice()).append("</price>");
				sb.append("<generalLocation>").append(asset.getGeneralLocation()).append("</generalLocation>");
				sb.append("<secure>").append(asset.getSecure()).append("</secure>");
				sb.append("</data>");
			} else {
				sb.append("<data>");
//				sb.append("<idLedger>").append("-1").append("</idLedger>");
				sb.append("</data>");
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(100);
			assetResponse.setResponseMessage("Error al buscar registro");
			sb.append(XmlFactory.getXml(idTransaction, assetResponse));
		}

		setResponse(request, response, sb.toString(), "application/xml");
	}

	public void insertAsset(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {

		AssetResponse assetResponse = new AssetResponse();
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		try {
			
			String idSubclass = gerParameterString(request,"idSubclass");
			if(idSubclass == null || idSubclass.equals("-1")){
				assetResponse.setResponseCode(99);
				assetResponse.setResponseMessage("La subclase no puede ser nula, por favor seleccione un valor valido");
			}else{
				Date date = sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
				
				long idLedger = gerParameterLong(request,"idLedger");
				
				
				String description = gerParameterString(request, "description");
				String brand = gerParameterString(request, "brand", "Sin marca");
				String model = gerParameterString(request, "model", "Sin modelo");
				String serialNumber = gerParameterString(request, "serialNumber", "Sin numero de serie");
				
				String material = gerParameterString(request,"material");
				String color = gerParameterString(request,"mColor");
				
				String supplier = gerParameterString(request, "supplier", "Proveedor no identificado");
				String generalManager = gerParameterString(request, "generalManager");
				String directlyResponsible = gerParameterString(request, "directlyResponsible");
				
	//			long tag = gerParameterLong(request,"tag");
				
				String bill = gerParameterString(request, "bill", "Sin factura");
				Date billingDate = getParameterDate(request, "billingDate", date);
				String location = gerParameterString(request, "location");
				Date useDate = getParameterDate(request, "useDate", date);
				
				float price = gerParameterFloat(request, "price");
				
				String generalLocation = gerParameterString(request, "generalLocation");
				String secure = gerParameterString(request, "secure");
	
				long tag = assetService.getTag(idTransaction, idLedger, idSubclass);
				if (tag == -1) {
					assetResponse.setResponseCode(100);
					assetResponse.setResponseMessage("Error al obtener el consecutivo de la etiqueta, por favor intente de nuevo");
				}else{			
					Asset asset = new Asset();
					asset.setIdLedger(idLedger);
					asset.setIdSubclass(idSubclass);
					asset.setTag(tag);
					asset.setDescription(description);
					asset.setBrand(brand);
					asset.setModel(model);
					asset.setSerialNumber(serialNumber);
					asset.setMaterial(material);
					asset.setColor(color);
					asset.setSupplier(supplier);
					asset.setGeneralManager(generalManager);
					asset.setDirectlyResponsible(directlyResponsible);
					asset.setBill(bill);
					asset.setBillingDate(billingDate);
					asset.setLocation(location);
					asset.setUseDate(useDate);
					asset.setPrice(price);
					asset.setGeneralLocation(generalLocation);
					asset.setSecure(secure);
					
					log.info(logPattern.buildPattern(methodName, idTransaction, "Asset", ToStringBuilder.reflectionToString(asset)));
		
					int responseCode = assetService.insertAsset(idTransaction,asset);
					if (responseCode == 1) {
						assetResponse.setResponseCode(0);
						assetResponse.setResponseMessage("Se inserto registro con la etiqueta " + tag);
					} else {
						assetResponse.setResponseCode(101);
						assetResponse.setResponseMessage("Error en la BD al insertar registro " + tag);
					}
				}
			}

		} catch (ParseException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
			assetResponse.setResponseCode(102);
			assetResponse.setResponseMessage("Error al interpretar la fecha");
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(103);
			assetResponse.setResponseMessage("Error al insertar registro");
		}
		setResponse(request, response, XmlFactory.getXml(idTransaction, assetResponse), "application/xml");
	}
	
	public void updateAsset(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {

		AssetResponse assetResponse = new AssetResponse();
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		try {
			
			long tag = gerParameterLong(request,"tag");
			if(tag == -1){
				assetResponse.setResponseCode(99);
				assetResponse.setResponseMessage("El numero de etiqueta no puede ser nulo");
			}else{
				Date date = sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
				
				String description = gerParameterString(request, "description");
				String brand = gerParameterString(request, "brand", "Sin marca");
				String model = gerParameterString(request, "model", "Sin modelo");
				String serialNumber = gerParameterString(request, "serialNumber", "Sin numero de serie");
				
				String material = gerParameterString(request,"material");
				String color = gerParameterString(request,"mColor");
				
				String supplier = gerParameterString(request, "supplier", "Proveedor no identificado");
				String generalManager = gerParameterString(request, "generalManager");
				String directlyResponsible = gerParameterString(request, "directlyResponsible");
				
				String bill = gerParameterString(request, "bill", "Sin factura");
				Date billingDate = getParameterDate(request, "billingDate", date);
				String location = gerParameterString(request, "location");
				Date useDate = getParameterDate(request, "useDate", date);
				
				float price = gerParameterFloat(request, "price");
				
				String generalLocation = gerParameterString(request, "generalLocation");
				String secure = gerParameterString(request, "secure");
	
				Asset asset = new Asset();
				asset.setTag(tag);
				asset.setDescription(description);
				asset.setBrand(brand);
				asset.setModel(model);
				asset.setSerialNumber(serialNumber);
				asset.setMaterial(material);
				asset.setColor(color);
				asset.setSupplier(supplier);
				asset.setGeneralManager(generalManager);
				asset.setDirectlyResponsible(directlyResponsible);
				asset.setBill(bill);
				asset.setBillingDate(billingDate);
				asset.setLocation(location);
				asset.setUseDate(useDate);
				asset.setPrice(price);
				asset.setGeneralLocation(generalLocation);
				asset.setSecure(secure);
				
				log.info(logPattern.buildPattern(methodName, idTransaction, "asset", ToStringBuilder.reflectionToString(asset)));
	
				int responseCode = assetService.updateAsset(idTransaction,asset);
				if (responseCode == 1) {
					assetResponse.setResponseCode(0);
					assetResponse.setResponseMessage("Se actualizo registro con la etiqueta " + tag);
				} else {
					assetResponse.setResponseCode(101);
					assetResponse.setResponseMessage("Error en la BD al actualizar registro");
				}
			}
		} catch (ParseException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
			assetResponse.setResponseCode(102);
			assetResponse.setResponseMessage("Error al interpretar la fecha");
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(103);
			assetResponse.setResponseMessage("Error al insertar registro");
		}
		setResponse(request, response, XmlFactory.getXml(idTransaction, assetResponse), "application/xml");
//		setResponse(request, response, sb);
	}
	
	public void getDirectlyResponsible(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response){
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		StringBuilder sb = new StringBuilder();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		try {
			Asset[] assetArray = assetService.getDirectlyResponsible(idTransaction);
			
			if(assetArray != null && assetArray.length > 0){
				sb.append("<data>");
				sb.append("<item value=\"").append(-1).append("\" label=\"").append("Seleccionar").append("\" />");
				for(Asset asset : assetArray){
	//				log.info(logPattern.buildPattern(methodName, idTransaction, "asset", ToStringBuilder.reflectionToString(asset)));
					if(asset != null){
						sb.append("<item value=\"").append(asset.getDirectlyResponsible()).append("\" label=\"").append(asset.getDirectlyResponsible()).append("\" />");
					}
				}
				sb.append("</data>");
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		
		setResponse(request, response, sb.toString(), "application/xml");
	}
	
	public void getDirectlyResponsibleAsset(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response){
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		StringBuilder sb = new StringBuilder();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		AssetResponse assetResponse = new AssetResponse();
		
		String directlyResponsible = gerParameterString(request, "directlyResponsible");
		
		try {
			Asset[] assetArray = assetService.getDirectlyResponsibleAsset(idTransaction, directlyResponsible);
			
			if (assetArray != null) {
				log.error(logPattern.buildPattern(methodName, idTransaction, "assetArray", assetArray.length));
				sb.append("<rows>");
				for (Asset asset : assetArray) {
					sb.append("<row id=\"" + asset.getId() + "\">");
//					sb.append("<cell type=\"sub_row_grid\">").append("xml/query_sgrid.xml").append("</cell>");
					sb.append("<cell>").append(asset.getTag()).append("</cell>");
					sb.append("<cell>").append(asset.getSubclass()).append("</cell>");
					sb.append("<cell>").append(asset.getDescription()).append("</cell>");
					sb.append("<cell>").append(asset.getBrand()).append("</cell>");
					sb.append("<cell>").append(asset.getModel()).append("</cell>");
					sb.append("<cell>").append(asset.getSerialNumber()).append("</cell>");
//					sb.append("<cell>").append(asset.getMaterial()).append("</cell>");
//					sb.append("<cell>").append(asset.getColor()).append("</cell>");
//					sb.append("<cell>").append(asset.getSupplier()).append("</cell>");
//					sb.append("<cell>").append(asset.getDirectlyResponsible()).append("</cell>");
//					sb.append("<cell>").append(asset.getGeneralManager()).append("</cell>");
//					sb.append("<cell>").append(asset.getTag()).append("</cell>");
//					sb.append("<cell>").append(asset.getBill()).append("</cell>");
//					sb.append("<cell>").append(sdf.format(asset.getBillingDate())).append("</cell>");
					sb.append("<cell>").append(asset.getPrice()).append("</cell>");
//					sb.append("<cell>").append(sdf.format(asset.getUseDate())).append("</cell>");
//					sb.append("<cell>").append(asset.getLocation()).append("</cell>");
//					sb.append("<cell>").append(asset.getGeneralLocation()).append("</cell>");
//					sb.append("<cell>").append(asset.getSecure()).append("</cell>");
					sb.append("</row>");
				}
				sb.append("</rows>");
			} else {
				assetResponse.setResponseCode(100);
				assetResponse.setResponseMessage("No se encontraron registros");
				sb.append(XmlFactory.getXml(idTransaction, assetResponse));
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(101);
			assetResponse.setResponseMessage("Error al obtener registro");
			sb.append(XmlFactory.getXml(idTransaction, assetResponse));
		}

		setResponse(request, response, sb.toString(), "application/xml");
	}
	
	public void setResponse(HttpServletRequest request,
			HttpServletResponse response, String res, String contentType) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
	
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//		response.setContentType("application/xml");
		response.setContentType(contentType);
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache
	
		PrintWriter out;
	
		try {
			out = response.getWriter();
			out.println(res);
		} catch (IOException e) {
//			log.error(logPattern.buildPattern(methodName, idTransaction, "response", res));
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage(), res), e);
		}
	
	}
	
	public String gerParameterString(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name).toUpperCase().trim() : null;
	}
	
	public String gerParameterString(HttpServletRequest request, String name, String def){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name).toUpperCase().trim() : def.toUpperCase().trim();
	}
	
	public int gerParameterInt(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Integer.parseInt(request.getParameter(name)) : -1;
	}
	
	public long gerParameterLong(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Long.parseLong(request.getParameter(name)) : -1;
	}
	
	public float gerParameterFloat(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Float.parseFloat(request.getParameter(name)) : -1;
	}
	
	public Date getParameterDate(HttpServletRequest request, String name) throws ParseException{
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? sdf.parse(request.getParameter(name)) : null;
	}
	
	public Date getParameterDate(HttpServletRequest request, String name, Date def) throws ParseException{
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? sdf.parse(request.getParameter(name)) : def;
	}
}
