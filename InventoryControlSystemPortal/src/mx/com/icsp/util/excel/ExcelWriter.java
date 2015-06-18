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

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
	
	public void generate(String idTransaction, HttpServletRequest request, HttpServletResponse response, String xml){
		
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
//		log.info(logPattern.buildPattern(methodName, idTransaction, "xml", xml));
		parser = new ExcelXmlParser();
		try {
			fileName = request.getParameter("fileName");
			extension = request.getParameter("extension");
			parser.setXML(xml);
			createExcel();
			rowsPrint(parser, response);
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
		
//		log.info(logPattern.buildPattern(methodName, idTransaction, "xml", xml));
		parser = new ExcelXmlParser();
		try {
			fileName = request.getParameter("fileName");
			extension = request.getParameter("extension");
			parser.setXML(null);
			createExcel();
			rowsPrint(parser, response);
			outputExcel(idTransaction, response, fileName+"."+extension);
		} catch (DOMException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "DOMException", e.getMessage()), e);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		} catch (ParserConfigurationException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParserConfigurationException", e.getMessage()), e);
		} catch (SAXException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "SAXException", e.getMessage()), e);
		}
	
	}
	
	private void createExcel(){
		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Activo");
	}
	
	private void rowsPrint(ExcelXmlParser parser, HttpServletResponse response) {
		//do we really need them?
		ExcelRow[] rows = parser.getGridContent();
		for (int i = 0; i < rows.length; i++) {
			
			Row rowData = sheet.createRow(i);
			
			ExcelCell[] cells = rows[i].getCells();
			for (int j = 0; j < cells.length; j++) {
				rowData.createCell(j).setCellValue(cells[j].getValue());
			}
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
