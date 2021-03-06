package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import mx.com.icsp.util.Constants;
import mx.com.icsp.util.CreateXML;
import mx.com.icsp.util.excel.ExcelWriter;
import mx.com.icsp.util.pdf.PdfAssetWriter;
import mx.com.icsp.util.pdf.PdfGuardWriter;
import mx.com.icsp.util.pdf.PdfWriter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ReportAction extends DispatchAction{
	
	private Logger log = Logger.getLogger(this.getClass());
	private LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	public static final String CONTENT_TYPE_EXCEL = "application/ms-excel";
	public static final String HEADER_1 = "Content-Disposition";
	public static final String HEADER_2 = "attachment; filename=";
	public static final String CONTENT_TYPE_PDF = "application/pdf";
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	AssetService assetService;
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	
	public void getAssetGridFilter(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		AssetResponse assetResponse = new AssetResponse();
		StringBuilder sb = new StringBuilder();
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		try {
			
			String query_radio = getParameterString(request, "query_radio");
			long tag = getParameterLong(request, "tag");
			Date startBillingDate = getParameterDate(request, "startBillingDate");
			Date endBillingDate = getParameterDate(request, "endBillingDate");
			Date startUseDate = getParameterDate(request, "startUseDate");
			Date endUseDate = getParameterDate(request, "endUseDate");
			
			Object[] params = new Object[]{query_radio, tag, startBillingDate, endBillingDate, startUseDate, endUseDate};
			
			log.info(logPattern.buildPattern(methodName, idTransaction, "params", ToStringBuilder.reflectionToString(params)));
			
			Asset[] assetArray = null;
			if(query_radio.toUpperCase().equals("TAG")){
				Asset asset = assetService.getAssetByTag(idTransaction, tag);
				
				if(asset != null)
					assetArray = new Asset[]{asset};
			}else if(query_radio.toUpperCase().equals("BILLINGDATE")){
				assetArray = assetService.getAssetByBillingDate(idTransaction, startBillingDate, endBillingDate);
			}else if(query_radio.toUpperCase().equals("USEDATE")){
				assetArray = assetService.getAssetByUseDate(idTransaction, startUseDate, endUseDate);
			}
			
			if(assetArray != null && assetArray.length > 0){
				sb.append(CreateXML.buildGeneralGrid(assetArray));
			}else{
				assetResponse.setResponseCode(0);
				assetResponse.setResponseMessage("No se encontraron registros con los datos ingresados");
				sb.append(XmlFactory.getXml(idTransaction, assetResponse));
			}
			
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(100);
			assetResponse.setResponseMessage("Error al consultar registros");
			sb.append(XmlFactory.getXml(idTransaction, assetResponse));
		}
		
		setResponse(request, response, sb.toString(), "application/xml");
	}
	
	public void getAssetReport(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		long tag = getParameterLong(request, "tag");
		log.info(logPattern.buildPattern(methodName, idTransaction, "tag", tag));
		
		try {
			Asset asset = assetService.getAssetByTag(idTransaction, tag);
			if(asset != null)
				new PdfAssetWriter().generate(idTransaction, request, response, asset);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		}
	}
	
	public void getAssetGridPdf(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String grid_xml = request.getParameter("grid_xml");
		String fileName = request.getParameter("fileName");
		log.info(logPattern.buildPattern(methodName, idTransaction, "fileName", fileName));
		
		String xml = null;
		try {
			xml = URLDecoder.decode(grid_xml, "UTF-8");
			new PdfWriter().generate(idTransaction, request, response, xml);
		} catch (UnsupportedEncodingException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "UnsupportedEncodingException", e.getMessage(), xml), e);
			setResponseError(request, response, e.getMessage());
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage(), grid_xml), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		}
	}
	
	public void getAssetGridExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String grid_xml = request.getParameter("grid_xml");
		String xml = null;
		try {
			xml = URLDecoder.decode(grid_xml, "UTF-8");
			new ExcelWriter().generate(idTransaction, request, response, xml);
		} catch (UnsupportedEncodingException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "UnsupportedEncodingException", e.getMessage(), xml), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage(), grid_xml), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		}
	}
	
	public void getAssetDbExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String fileName = request.getParameter("fileName");
		String extension = request.getParameter("extension");
		
		String[] params = new String[]{fileName, extension};
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "params", ToStringBuilder.reflectionToString(params)));
		
		try {
			Asset[] assetArray = assetService.getAsset(idTransaction);
			new ExcelWriter().generate(idTransaction, request, response, assetArray);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		}
	}
	
	public void getGuardPdf(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		try {
			String directlyResponsible = getParameterString(request, "directlyResponsible", "");
			String costCenter = getParameterString(request, "costCenter", "");
			String area = getParameterString(request, "area", "");
			String department = getParameterString(request, "department", "");
			
			String[] params = new String[]{directlyResponsible, costCenter, area, department, request.getParameter("date")};
			log.info(logPattern.buildPattern(methodName, idTransaction, "params", ToStringBuilder.reflectionToString(params)));
			
			Date date = getParameterDate(request, "date", new Date());
			Asset[] assetArray = assetService.getDirectlyResponsibleAsset(idTransaction, directlyResponsible);
			
			if(assetArray != null && assetArray.length >0)
				directlyResponsible = assetArray[0].getDirectlyResponsible();
			
			new PdfGuardWriter().generate(idTransaction, request, response, assetArray, directlyResponsible, costCenter, area, department, date);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append("showResponseXmlAlertError('").append("Error al generar el reporte ").append(e.getMessage()).append("');");
			sb.append("</script>");
			
			setResponseError(request, response, sb.toString());
		}
	}
	
	public void setResponse(HttpServletRequest request,
			HttpServletResponse response, String res, String contentType) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
	
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setContentType(contentType);
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache
	
		PrintWriter out;
	
		try {
			out = response.getWriter();
			out.println(res);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage(), res), e);
		}	
	}
	
	public void setResponseError(HttpServletRequest request, HttpServletResponse response, String msg){
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setContentType("application/javascript");
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache

		PrintWriter out;
		
		try {
			out = response.getWriter();
			out.println(msg);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage(), msg), e);
		}
	}
	
	public String getParameterString(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name).toUpperCase().trim() : null;
	}
	
	public String getParameterString(HttpServletRequest request, String name, String def){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name).toUpperCase().trim() : def.toUpperCase().trim();
	}
	
	public int getParameterInt(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Integer.parseInt(request.getParameter(name)) : -1;
	}
	
	public long getParameterLong(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Long.parseLong(request.getParameter(name)) : -1;
	}
	
	public float getParameterFloat(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Float.parseFloat(request.getParameter(name)) : -1;
	}
	
	public Date getParameterDate(HttpServletRequest request, String name) throws ParseException{
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? sdf.parse(request.getParameter(name)) : null;
	}
	
	public Date getParameterDate(HttpServletRequest request, String name, Date def) throws ParseException{
		return request.getAttribute(name) != null ? (Date)request.getAttribute(name) : def;
	}
}
