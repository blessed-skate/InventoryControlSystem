package mx.com.icsp.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.util.Constants;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;



public class ExcelWriter{
	
	private Logger log = Logger.getLogger(this.getClass());
	private LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	private ExcelXmlParser parser;
	private Workbook wb;
	private Sheet sheet;
	private String fileName, extension;
	private int header;
	private CellStyle cellStyleDate, styleBorder;
	private CreationHelper creationHelper;
	private Font font;
	
	public void generate(String idTransaction, HttpServletRequest request, HttpServletResponse response, String xml){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		parser = new ExcelXmlParser();
		try {
			header = request.getParameter("header") != null && !request.getParameter("header").trim().equals("") ? Integer.parseInt(request.getParameter("header")) : 0;
			fileName = request.getParameter("fileName");
			extension = request.getParameter("extension");
			Object[] params = new Object[]{header, fileName, extension};
			log.info(logPattern.buildPattern(methodName, idTransaction, "params",ToStringBuilder.reflectionToString(params)));
			
			parser.setXML(xml);
			createExcel();
			if(header == 1)
				createHeader();
			rowsPrint(parser);
			autoSizeColumn();
			outputExcel(idTransaction, response, fileName+"."+extension);
		} catch (DOMException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "DOMException", e.getMessage(), xml), e);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage(), xml), e);
		} catch (ParserConfigurationException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParserConfigurationException", e.getMessage(), xml), e);
		} catch (SAXException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "SAXException", e.getMessage(), xml), e);
		}
	
	}
	
	public void generate(String idTransaction, HttpServletRequest request, HttpServletResponse response, Asset[] assetArray){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		try {
			fileName = request.getParameter("fileName");
			extension = request.getParameter("extension");
			createExcel();
			createHeader();
			rowsPrint(assetArray);
			autoSizeColumn();
			outputExcel(idTransaction, response, fileName+"."+extension);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
	
	private void createExcel(){
		wb = new HSSFWorkbook();
		cellStyleDate = wb.createCellStyle();
		styleBorder = wb.createCellStyle();
		font = wb.createFont();
		
		styleBorder.setBorderBottom(CellStyle.BORDER_MEDIUM_DASHED);
		styleBorder.setBorderLeft(CellStyle.BORDER_THIN);
		styleBorder.setBorderRight(CellStyle.BORDER_THIN);
		styleBorder.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		
		styleBorder.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleBorder.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleBorder.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleBorder.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		
		styleBorder.setAlignment(CellStyle.ALIGN_CENTER);
		styleBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		styleBorder.setWrapText(true);
		
		font.setBold(true);
		styleBorder.setFont(font);
		
		creationHelper = wb.getCreationHelper();
		cellStyleDate.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		sheet = wb.createSheet("Activo");
	}
	
	private void createHeader(){
		Row row = sheet.createRow(0);
		row.setHeightInPoints(30);
		row.createCell(0).setCellValue("Cuenta contable");
		row.createCell(1).setCellValue("Tipo de bien mueble");
		row.createCell(2).setCellValue("Descripción");
		row.createCell(3).setCellValue("Marca");
		row.createCell(4).setCellValue("Modelo");
		row.createCell(5).setCellValue("Número de serie");
		row.createCell(6).setCellValue("Material");
		row.createCell(7).setCellValue("Color");
		row.createCell(8).setCellValue("Proveedor");
		row.createCell(9).setCellValue("Usuario");
		row.createCell(10).setCellValue("Jefe de usuario");
		row.createCell(11).setCellValue("Número de etiqueta");
		row.createCell(12).setCellValue("Número de factura");
		row.createCell(13).setCellValue("Fecha");
		row.createCell(14).setCellValue("Monto y/o valor estimado");
		row.createCell(15).setCellValue("Fecha de uso");
		row.createCell(16).setCellValue("Localización");
		row.createCell(17).setCellValue("Ubicación");
		row.createCell(18).setCellValue("Seguro");
		
		for(int i=0; i < row.getLastCellNum(); i++){
			row.getCell(i).setCellStyle(styleBorder);
		}
	}
	
	private void rowsPrint(Asset[] assetArray) {
		// TODO Auto-generated method stub
		int i=1;
		for(Asset asset : assetArray){
			Row rowData = sheet.createRow(i++);
			
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
			
			Cell cellDate1 = rowData.createCell(13);
			cellDate1.setCellStyle(cellStyleDate);
			cellDate1.setCellValue(asset.getBillingDate());
			
			Cell cellNumeric = rowData.createCell(14);
			cellNumeric.setCellType(Cell.CELL_TYPE_NUMERIC);
			cellNumeric.setCellValue(asset.getPrice());
			
			Cell cellDate2 = rowData.createCell(15);
			cellDate2.setCellStyle(cellStyleDate);
			cellDate2.setCellValue(asset.getUseDate());
			
			rowData.createCell(16).setCellValue(asset.getLocation());
			rowData.createCell(17).setCellValue(asset.getGeneralLocation());
			rowData.createCell(18).setCellValue(asset.getSecure());
		}
	}
	
	private void rowsPrint(ExcelXmlParser parser) {

		ExcelRow[] rows = parser.getGridContent();
		for (int i = 0; i < rows.length; i++) {
			
			Row rowData = sheet.createRow(i+header);
			
			ExcelCell[] cells = rows[i].getCells();
			for (int j = 0; j < cells.length; j++) {
				rowData.createCell(j).setCellValue(cells[j].getValue());
			}
		}
	}
	
	private void autoSizeColumn(){
		for(int i=0; i < sheet.getRow(0).getLastCellNum(); i++){
			sheet.autoSizeColumn(i);
		}
	}
	
	private void outputExcel(String idTransaction, HttpServletResponse response, String fileName){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/ms-excel");
		response.setHeader("Expires", "0");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);// set HTTP/1.0 no-cache

		OutputStream out;

		try {
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			
			out = response.getOutputStream();
			out.write(outArray);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		}
	}
}
