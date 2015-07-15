package mx.com.icsp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.ImportExcelResponse;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.PropertyServiceImpl;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CreateXML {
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// public static void main(String[] args){
	// String file = "a,b,c \n e,f,g";
	// createXml(file);
	// }

	public StringBuilder createXml(String file) {
		StringBuilder sb = null;
		int j = 0;
		if (file != null) {
			log.info("[CreateXML] File: " + file);
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" enconding=\"UTF-8\" ?>");
			sb.append("<rows>");
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String oneLine = scan.nextLine();
				String[] tokens = oneLine.split(",");
				sb.append("<row id=\"").append(j).append("\">");
				for (int i = 0; i < tokens.length; i++) {
					sb.append("<cell>").append(tokens[i].trim())
							.append("</cell>");
				}
				sb.append("</row>");
				j++;
			}
			sb.append("</rows>");
			log.info("SB: " + sb.toString());
			return sb;
		}
		return sb;
	}

	public void createGrid(String idTransaction, ImportExcelResponse response, Sheet sheet, List<Asset> assetList) {
		Iterator<Row> rowIterator = sheet.iterator();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		int rowInt = -1, columnInt = -1;
		
		try {
			if(response.isHeader())
				rowIterator.next();
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				rowInt = row.getRowNum();
				Asset asset = new Asset();
				
				int EXCEL_COLUMN_LENGTH = Integer.parseInt(PropertyServiceImpl.map.get("EXCEL_COLUMN_LENGTH").getValue());
				
				for(int i = 0; i < EXCEL_COLUMN_LENGTH; i++){
					Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
					columnInt = cell.getColumnIndex();
					
					switch (columnInt) {
					case Constants.COLUMN_ID_LEDGER:
						asset.setIdLedger(getCellLongValue(cell, 0, 3));
						asset.setIdSubclass(getCellStringValue(cell, 3, 5));
						asset.setTag(getCellLongValue(cell));
						break;

					case Constants.COLUMN_ASSET_TYPE:
						asset.setSubclass(getCellStringValue(cell));
						break;

					case Constants.COLUMN_DESCRIPTION:
						asset.setDescription(getCellStringValue(cell));
						break;

					case Constants.COLUMN_BRAND:
						asset.setBrand(getCellStringValue(cell, "Sin marca"));
						break;

					case Constants.COLUMN_MODEL:
						asset.setModel(getCellStringValue(cell, "Sin modelo"));
						break;

					case Constants.COLUMN_SERIAL_NUMBER:
						asset.setSerialNumber(getCellStringValue(cell, "Sin numero de serie"));
						break;

					case Constants.COLUMN_MATERIAL:
						asset.setMaterial(getCellStringValue(cell));
						break;

					case Constants.COLUMN_COLOR:
						asset.setColor(getCellStringValue(cell));
						break;

					case Constants.COLUMN_SUPPLIER:
						asset.setSupplier(getCellStringValue(cell, "Proveedor no identificado"));
						break;

					case Constants.COLUMN_DIRECTLY_RESPONSIBLE:
						asset.setDirectlyResponsible(getCellStringValue(cell));
						break;

					case Constants.COLUMN_GENERAL_MANAGER:
						asset.setGeneralManager(getCellStringValue(cell));
						break;

					case Constants.COLUMN_BILL:
						asset.setBill(getCellStringValue(cell, "Sin factura"));
						break;

					case Constants.COLUMN_BILL_DATE:
						asset.setBillingDate(getCellDateValue(cell, "setBillingDate", rowInt));
						break;

					case Constants.COLUMN_PRICE:
						asset.setPrice((float) getCellDoubleValue(cell));
						break;

					case Constants.COLUMN_USE_DATE:
						asset.setUseDate(getCellDateValue(cell, "setUseDate", rowInt));
						break;

					case Constants.COLUMN_PLACE:
						asset.setPlace(getCellStringValue(cell));
						break;

					case Constants.COLUMN_LOCATION:
						asset.setLocation(getCellStringValue(cell));
						break;

					case Constants.COLUMN_GENERAL_LOCATION:
						asset.setGeneralLocation(getCellStringValue(cell));
						break;

					case Constants.COLUMN_SECURE:
						asset.setSecure(getCellStringValue(cell));
						break;

					case Constants.COLUMN_START:
						asset.setStart(getCellStringValue(cell));
						break;
					}
				}
				assetList.add(asset);
			}
		} catch(IllegalStateException e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "IllegalStateException", e.getMessage()), e);
			String value = "null";
			switch(sheet.getRow(rowInt).getCell(columnInt).getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf(sheet.getRow(rowInt).getCell(columnInt).getNumericCellValue());
				break;
				case Cell.CELL_TYPE_STRING:
					value = sheet.getRow(rowInt).getCell(columnInt).getStringCellValue();
				break;
			}
			response.setInfo("Error de formato: " + e.getMessage() + " - [columna=" + (columnInt+1) + ", fila=" + (rowInt+1) + ", value="+value+"]");
			return;
		} catch (NumberFormatException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			response.setInfo("Error de formato de número: " + e.getMessage() + " - [columna=" + (columnInt+1) + ", fila=" + (rowInt+1) + "]");
			return;
		}  catch (ParseException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			response.setInfo("Error de formato de fecha: " + e.getMessage() + " - [columna=" + (columnInt+1) + ", fila=" + (rowInt+1) + "]");
			return;
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			response.setInfo("Error de sistema: " + e.getMessage() + " - [columna=" + (columnInt+1) + ", fila=" + (rowInt+1) + "]");
			return;
		}
		
		int cont = 1;
		try{
			StringBuilder sb = new StringBuilder();
			sb .append("<rows>");
			for(Asset asset : assetList){
				sb.append("<row>");					
				sb.append("<cell>").append(asset.getTag()).append("</cell>");
//				sb.append("<cell>").append(asset.getIdLedger()).append("</cell>");
				sb.append("<cell>").append(asset.getSubclass()).append("</cell>");
				sb.append("<cell>").append(asset.getDescription() != null ? asset.getDescription() : "").append("</cell>");
				sb.append("<cell>").append(asset.getBrand()).append("</cell>");
				sb.append("<cell>").append(asset.getModel()).append("</cell>");
				sb.append("<cell>").append(asset.getSerialNumber()).append("</cell>");
				sb.append("<cell>").append(asset.getMaterial()).append("</cell>");
				sb.append("<cell>").append(asset.getColor()).append("</cell>");
				sb.append("<cell>").append(asset.getSupplier()).append("</cell>");
				sb.append("<cell>").append(asset.getDirectlyResponsible() != null ? asset.getDirectlyResponsible() : "").append("</cell>");
				sb.append("<cell>").append(asset.getGeneralManager() != null ? asset.getGeneralManager() : "").append("</cell>");
				sb.append("<cell>").append(asset.getBill()).append("</cell>");
				sb.append("<cell>").append(sdf.format(asset.getBillingDate())).append("</cell>");
				sb.append("<cell>").append(asset.getPrice()).append("</cell>");
				sb.append("<cell>").append(sdf.format(asset.getUseDate())).append("</cell>");
				sb.append("<cell>").append(asset.getPlace() != null ? asset.getPlace() : "").append("</cell>");
				sb.append("<cell>").append(asset.getLocation() != null ? asset.getLocation() : "").append("</cell>");
				sb.append("<cell>").append(asset.getGeneralLocation() != null ? asset.getGeneralLocation() : "").append("</cell>");
				sb.append("<cell>").append(asset.getSecure() != null ? asset.getSecure() : "").append("</cell>");
				sb.append("<cell>").append(asset.getStart() != null ? asset.getStart() : "").append("</cell>");
				sb.append("</row>");
				cont++;
			}
			sb.append("</rows>");
			response.setParam(sb.toString());
			response.setInfo("Exito al cargar el archivo");
			response.setState(true);	
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			response.setInfo("Error de sistema: " + e.getMessage() + " - Error en el registro " + cont);
			return;
		}		
	}
	
	private String getCellStringValue(Cell cell, int i, int j) {
		String value = null;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf((long)cell.getNumericCellValue()).substring(i, j);
				break;
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue().substring(i, j);
				break;
				case Cell.CELL_TYPE_BLANK:
				break;
				case Cell.CELL_TYPE_ERROR:
					value = String.valueOf(cell.getErrorCellValue());
				break;
			}
		}
		return value;
	}

	private long getCellLongValue(Cell cell, int i, int j) {
		long value = 0;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = Long.parseLong(String.valueOf((long) cell.getNumericCellValue()).substring(i, j));
				break;
				case Cell.CELL_TYPE_STRING:
					value = Long.parseLong(cell.getStringCellValue() != null ? cell.getStringCellValue().substring(i, j) : "0");
				break;
				case Cell.CELL_TYPE_BLANK:
					value = 0;
				break;
				case Cell.CELL_TYPE_ERROR:
					value = 0;
				break;
			}
		}
		return value;
	}

	public String getCellStringValue(Cell cell){
		String value = null;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf((long)cell.getNumericCellValue());
				break;
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
				break;
				case Cell.CELL_TYPE_BLANK:
				break;
				case Cell.CELL_TYPE_ERROR:
					value = String.valueOf(cell.getErrorCellValue());
				break;
			}
		}
		return value;
	}
	
	public String getCellStringValue(Cell cell, String defaultValue){
		String value = null;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf((long)cell.getNumericCellValue());
				break;
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
				break;
				case Cell.CELL_TYPE_BLANK:
				break;
				case Cell.CELL_TYPE_ERROR:
					value = String.valueOf(cell.getErrorCellValue());
				break;
			}
		}
		
		if(value == null || value.trim().equals("") || value.trim().toUpperCase().equals("SN"))
			value = defaultValue;
			
		return value;
	}
	
	public double getCellDoubleValue(Cell cell){
		double value = 0.0;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = cell.getNumericCellValue();
				break;
				case Cell.CELL_TYPE_STRING:
					value = Double.parseDouble(cell.getStringCellValue() != null ? cell.getStringCellValue() : "0.0");
				break;
				case Cell.CELL_TYPE_BLANK:
					value = 0.0;
				break;
				case Cell.CELL_TYPE_ERROR:
					value = 0.0;
				break;
			}
		}
		return value;
	}
	
	public long getCellLongValue(Cell cell){
		long value = 0;
		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:
					value = (long) cell.getNumericCellValue();
				break;
				case Cell.CELL_TYPE_STRING:
					value = Long.parseLong(cell.getStringCellValue() != null ? cell.getStringCellValue() : "0");
				break;
				case Cell.CELL_TYPE_BLANK:
					value = 0;
				break;
				case Cell.CELL_TYPE_ERROR:
					value = 0;
				break;
			}
		}
		return value;
	}
	
	public Date getCellDateValue(Cell cell, String text, int row) throws ParseException{
		Date defaultDate = sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());

		if(cell != null){
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_STRING:
					if(cell.getStringCellValue() != null && !cell.getStringCellValue().equals("")){
						if(cell.getStringCellValue().trim().toUpperCase().equals("SN") || cell.getStringCellValue().trim().equals("")){
							defaultDate = sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
						}else{
							defaultDate = sdf.parse(cell.getStringCellValue() != null ? cell.getStringCellValue() : PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
						}
					}
				break;
				case Cell.CELL_TYPE_NUMERIC:
				    if (HSSFDateUtil.isCellDateFormatted(cell)) { 
				        defaultDate = cell.getDateCellValue();
				    }
				break;
				default:
					defaultDate = cell.getDateCellValue() != null ? cell.getDateCellValue() : sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
				break;
			}
		}
		
		if(defaultDate == null)
			defaultDate = sdf.parse(PropertyServiceImpl.map.get("DEFAULT_DATE").getValue());
		
		
		return defaultDate;
	}
}
