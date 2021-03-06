package mx.com.icsp.util.excel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.PropertyServiceImpl;
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
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
	private CellStyle styleDate, styleHeader, styleDecimalNumber;
	private CreationHelper creationHelper;
	private Font font;
	int EXCEL_COLUMN_LENGTH = Integer.parseInt(PropertyServiceImpl.map.get("EXCEL_COLUMN_LENGTH").getValue());
	
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
			log.info(logPattern.buildPattern(methodName, idTransaction, "assetArray", assetArray.length));
			fileName = request.getParameter("fileName");
			extension = request.getParameter("extension");
			log.info(logPattern.buildPattern(methodName, idTransaction, "createExcel"));
			createExcel();
			log.info(logPattern.buildPattern(methodName, idTransaction, "createHeader"));
			createHeader();
			log.info(logPattern.buildPattern(methodName, idTransaction, "rowsPrint"));
			rowsPrint(assetArray);
			log.info(logPattern.buildPattern(methodName, idTransaction, "autoSizeColumn"));
			autoSizeColumn();
			log.info(logPattern.buildPattern(methodName, idTransaction, "outputExcel"));
			outputExcel(idTransaction, response, fileName+"."+extension);
			log.info(logPattern.buildPattern(methodName, idTransaction, "End outputExcel..."));
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
	
	private void createExcel(){
		if(extension.endsWith("xlsx")){
			wb = new SXSSFWorkbook();
			((SXSSFWorkbook) wb).setCompressTempFiles(true);
			sheet = (SXSSFSheet) wb.createSheet("Activo");
			((SXSSFSheet) sheet).setRandomAccessWindowSize(500);
		}
		else{
			wb = new HSSFWorkbook();
			sheet = (Sheet) wb.createSheet("Activo");
		}
		
		styleDate = wb.createCellStyle();
		styleHeader = wb.createCellStyle();
		styleDecimalNumber = wb.createCellStyle();
		
		font = wb.createFont();
		
		styleHeader.setBorderBottom(CellStyle.BORDER_MEDIUM_DASHED);
		styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		styleHeader.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		
		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		styleHeader.setWrapText(true);
		styleHeader.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		font.setBold(true);
		styleHeader.setFont(font);
		
		creationHelper = wb.getCreationHelper();
		styleDate.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		
		styleDecimalNumber.setDataFormat(creationHelper.createDataFormat().getFormat("$#,##0.00"));
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
		row.createCell(11).setCellValue("Número de factura");
		row.createCell(12).setCellValue("Fecha");
		row.createCell(13).setCellValue("Monto y/o valor estimado");
		row.createCell(14).setCellValue("Fecha de uso");
		row.createCell(15).setCellValue("Lugar");
		row.createCell(16).setCellValue("Localización");
		row.createCell(17).setCellValue("Ubicación");
		row.createCell(18).setCellValue("Seguro");
		row.createCell(19).setCellValue("Incio");
		
		for(int i=0; i < row.getLastCellNum(); i++){
			row.getCell(i).setCellStyle(styleHeader);
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
			rowData.createCell(11).setCellValue(asset.getBill());
			
			Cell cellDate1 = rowData.createCell(12);
			cellDate1.setCellStyle(styleDate);
			cellDate1.setCellValue(asset.getBillingDate());
			
			Cell cellNumeric = rowData.createCell(13);
			cellNumeric.setCellStyle(styleDecimalNumber);
			cellNumeric.setCellValue(asset.getPrice());
			
			Cell cellDate2 = rowData.createCell(14);
			cellDate2.setCellStyle(styleDate);
			cellDate2.setCellValue(asset.getUseDate());
			
			rowData.createCell(15).setCellValue(asset.getPlace());
			rowData.createCell(16).setCellValue(asset.getLocation());
			rowData.createCell(17).setCellValue(asset.getGeneralLocation());
			rowData.createCell(18).setCellValue(asset.getSecure());
			rowData.createCell(19).setCellValue(asset.getStart());
		}
	}
	
	private void rowsPrint(ExcelXmlParser parser) {

		ExcelRow[] rows = parser.getGridContent();
		for (int i = 0; i < rows.length; i++) {
			
			Row rowData = sheet.createRow(i+header);
			Cell cellDate, cellNumeric;
			
			ExcelCell[] cells = rows[i].getCells();
			for (int j = 0; j < cells.length; j++) {
//				rowData.createCell(j).setCellValue(cells[j].getValue());
				
				switch (j) {
				case Constants.COLUMN_ID_LEDGER:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_ASSET_TYPE:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_DESCRIPTION:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_BRAND:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_MODEL:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_SERIAL_NUMBER:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_MATERIAL:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_COLOR:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_SUPPLIER:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_DIRECTLY_RESPONSIBLE:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_GENERAL_MANAGER:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_BILL:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_BILL_DATE:
					cellDate = rowData.createCell(j);
					cellDate.setCellStyle(styleDate);
					cellDate.setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_PRICE:
					cellNumeric = rowData.createCell(j);
					cellNumeric.setCellStyle(styleDecimalNumber);
					cellNumeric.setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_USE_DATE:
					cellDate = rowData.createCell(j);
					cellDate.setCellStyle(styleDate);
					cellDate.setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_PLACE:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_LOCATION:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_GENERAL_LOCATION:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_SECURE:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;

				case Constants.COLUMN_START:
					rowData.createCell(j).setCellValue(cells[j].getValue());
					break;
				}
			
			}
		}
	}
	
	private void autoSizeColumn(){
		for(int i=0; i < EXCEL_COLUMN_LENGTH; i++){
			sheet.autoSizeColumn(i);
		}
	}
	
	private void outputExcel(String idTransaction, HttpServletResponse response, String fileName){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		response.setCharacterEncoding("UTF-8");
		if(extension.endsWith("xlsx"))
			response.setContentType("application/vnd.ms-excel");
		else
			response.setContentType("application/x-ms-excel"); // or application/x-ms-excel"
		
		response.setHeader("Expires", "0");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);// set HTTP/1.0 no-cache

		try {
			wb.write(response.getOutputStream());
			wb.close();
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		}
	}
}
