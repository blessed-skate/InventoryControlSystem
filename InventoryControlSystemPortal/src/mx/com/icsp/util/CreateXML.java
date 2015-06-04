package mx.com.icsp.util;

import java.util.Iterator;
import java.util.Scanner;

import mx.com.icsc.common.util.LogPattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CreateXML {
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode, Constants.solutioNameCode, Constants.platform, Constants.tower, this.getClass().getName());

	//	public static void main(String[] args){
	//		String file = "a,b,c \n e,f,g";
	//		createXml(file);
	//	}

	public StringBuilder createXml(String file){
		StringBuilder sb = null;
		int j = 0;
		if(file != null){
			log.info("[CreateXML] File: " + file);
			sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" enconding=\"UTF-8\" ?>");
			sb.append("<rows>");
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()){				
				String oneLine = scan.nextLine();
				String[] tokens = oneLine.split(",");
				sb.append("<row id=\"").append(j).append("\">");
				for (int i = 0 ; i < tokens.length; i++) {
					sb.append("<cell>").append(tokens[i].trim()).append("</cell>");
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
	
	public String createGrid(Sheet sheet){
        Iterator<Row> rowIterator = sheet.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("<rows>");
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            
            sb.append("<row>");
            
            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                sb.append("<cell>");     
                switch(cell.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                    	sb.append(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                    	sb.append(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                    	sb.append(cell.getStringCellValue());
                        break;  
                }
                sb.append("</cell>");
            }
            sb.append("</row>");
        }
        sb.append("</rows>");
		
		return sb.toString();
	}
}
