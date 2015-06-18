package mx.com.icsp.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.AssetService;
import mx.com.icsp.util.Constants;
import mx.com.icsp.util.excel.ExcelWriter;
import mx.com.icsp.util.pdf.PdfWriter;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	AssetService assetService;
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	
	public void getAssetXml(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Asset[] assetArray = assetService.getAsset(idTransaction);
		if(assetArray != null && assetArray.length > 0){
			log.info(logPattern.buildPattern(methodName, idTransaction, "assetArray", String.valueOf(assetArray.length)));
			try{
				Workbook wb = new HSSFWorkbook();
				
				CellStyle style = wb.createCellStyle();
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);
				style.setBorderTop(CellStyle.BORDER_THIN);
				
				Sheet sheet1 = wb.createSheet("Activo");
				sheet1.autoSizeColumn(1);
				sheet1.autoSizeColumn(2);
				sheet1.autoSizeColumn(3);
				sheet1.autoSizeColumn(4);
				sheet1.autoSizeColumn(5);
				sheet1.autoSizeColumn(6);
				sheet1.autoSizeColumn(7);
				sheet1.autoSizeColumn(8);
				sheet1.autoSizeColumn(9);
				sheet1.autoSizeColumn(10);
				sheet1.autoSizeColumn(11);
				sheet1.autoSizeColumn(12);
				sheet1.autoSizeColumn(13);
				sheet1.autoSizeColumn(14);
				sheet1.autoSizeColumn(15);
				sheet1.autoSizeColumn(16);
				sheet1.autoSizeColumn(17);
				sheet1.autoSizeColumn(18);
				sheet1.autoSizeColumn(19);
				
				Row row = sheet1.createRow(0);
				row.createCell(0).setCellValue("Número de cuenta contable:");
				row.createCell(1).setCellValue("Tipo de bien mueble:");
				row.createCell(2).setCellValue("Descripción:");
				row.createCell(3).setCellValue("Marca:");
				row.createCell(4).setCellValue("Modelo:");
				row.createCell(5).setCellValue("Número de serie:");
				row.createCell(6).setCellValue("Material:");
				row.createCell(7).setCellValue("Color:");
				row.createCell(8).setCellValue("Proveedor:");
				row.createCell(9).setCellValue("Usuario:");
				row.createCell(10).setCellValue("Jefe de usuario:");
				row.createCell(11).setCellValue("Número de etiqueta:");
				row.createCell(12).setCellValue("Número de factura:");
				row.createCell(13).setCellValue("Fecha:");
				row.createCell(14).setCellValue("Monto y/o valor estimado:");
				row.createCell(15).setCellValue("Fecha de uso:");
				row.createCell(16).setCellValue("Localización:");
				row.createCell(17).setCellValue("Ubicación:");
				row.createCell(18).setCellValue("Seguro:");
				
				row.setRowStyle(style);
				
				int i=1;
				for(Asset asset : assetArray){
					Row rowData = sheet1.createRow(i++);
					rowData.createCell(0).setCellValue(String.valueOf(asset.getTag()));
					rowData.createCell(1).setCellValue(asset.getSubclass());
					rowData.createCell(2).setCellValue(asset.getDescription());
					rowData.createCell(3).setCellValue(asset.getBrand());
					rowData.createCell(4).setCellValue(asset.getModel());
					rowData.createCell(5).setCellValue(asset.getSerialNumber());
					rowData.createCell(6).setCellValue(asset.getMaterial());
					rowData.createCell(7).setCellValue(asset.getColor());
					rowData.createCell(8).setCellValue(asset.getSupplier());
					rowData.createCell(9).setCellValue(asset.getDirectlyResponsible());
					rowData.createCell(10).setCellValue(asset.getGeneralManager());
					rowData.createCell(11).setCellValue(String.valueOf(asset.getTag()));
					rowData.createCell(12).setCellValue(asset.getBill());
					rowData.createCell(13).setCellValue(asset.getBillingDate());
					rowData.createCell(14).setCellValue(asset.getPrice());
					rowData.createCell(15).setCellValue(asset.getUseDate());
					rowData.createCell(16).setCellValue(asset.getLocation());
					rowData.createCell(17).setCellValue(asset.getGeneralLocation());
					rowData.createCell(18).setCellValue(asset.getSecure());
					rowData.setRowStyle(style);
				}
				setResponse(request, response, CONTENT_TYPE_EXCEL, "REPORTE_"+sdf.format(new Date())+".xlsx", wb);
			}catch(Exception e){
				log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			}
		}
	}
	
	public void getAssetGridPdf(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String param = request.getParameter("grid_xml");
		String fileName = request.getParameter("fileName");
		log.info(logPattern.buildPattern(methodName, idTransaction, "fileName", fileName));
		
		String xml = null;
		try {
			xml = URLDecoder.decode(param, "UTF-8");
			new PdfWriter().generate(idTransaction, request, response, xml);
		} catch (UnsupportedEncodingException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "UnsupportedEncodingException", e.getMessage(), xml), e);
		}
	}
	
	public void getAssetGridExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String param = request.getParameter("grid_xml");
		String fileName = request.getParameter("fileName");
		String header = request.getParameter("header");
		String extension = request.getParameter("extension");
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "fileName", fileName));
		log.info(logPattern.buildPattern(methodName, idTransaction, "header", header));
		log.info(logPattern.buildPattern(methodName, idTransaction, "extension", extension));
		String xml = null;
		try {
			xml = URLDecoder.decode(param, "UTF-8");
			new ExcelWriter().generate(idTransaction, request, response, xml);
		} catch (UnsupportedEncodingException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "UnsupportedEncodingException", e.getMessage(), xml), e);
		}
	}
	
	public void getAssetDbExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		String fileName = request.getParameter("fileName");
		String extension = request.getParameter("extension");
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "fileName", fileName));
		log.info(logPattern.buildPattern(methodName, idTransaction, "extension", extension));
		
		try {
			Asset[] assetArray = assetService.getAsset(idTransaction);
			new ExcelWriter().generate(idTransaction, request, response, assetArray);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
	
	public void setResponse(HttpServletRequest request, HttpServletResponse response, String contentType, String fileName, Workbook wb){
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		response.setHeader("Expires", "0");
		response.setHeader(HEADER_1, HEADER_2+fileName);// set HTTP/1.0 no-cache

		OutputStream out;

		try {
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			
			out = response.getOutputStream();
			out.write(outArray);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
}
