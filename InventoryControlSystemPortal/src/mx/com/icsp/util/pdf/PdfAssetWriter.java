package mx.com.icsp.util.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.User;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.util.Constants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

public class PdfAssetWriter {
	
	private Logger log = Logger.getLogger(this.getClass());
	private LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	private String fileName, userName = "";
	private User user;
	private JasperPrint print;
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public void generate(String idTransaction, HttpServletRequest request, HttpServletResponse response, Asset asset){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		try {
			user = request.getSession().getAttribute("user") != null ? (User)request.getSession().getAttribute("user") : new User();
			if(user.getName() != null && !user.getName().equals("")){
				userName = userName + user.getName();
				if(user.getLastName() != null && !user.getLastName().equals(""))
					userName = userName + user.getLastName();
			}else{
				userName = "Anonimo";
			}
			
			fileName = "REPORTE_"+asset.getTag();
			
			List<Asset> list = Arrays.asList(asset);
					
			createReport(idTransaction, list, asset.getTag());
			outputPdf(idTransaction, response, fileName);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
	
	private void createReport(String idTransaction, List<Asset> list, long tag) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			
			parameters.put("tag", tag);
			
			URL url = null;
			try{
				url = this.getClass().getResource("logo2.png");
				log.info(logPattern.buildPattern(methodName, idTransaction, "url", ToStringBuilder.reflectionToString(url)));
				ImageIcon img = new ImageIcon(url.getPath());
				parameters.put("image", img.getImage());
			}catch(Exception e){
				log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			}
			
			InputStream inputStream = this.getClass().getResourceAsStream("barcode.jrxml");
			JasperReport report;
			report = JasperCompileManager.compileReport(inputStream);

			print = JasperFillManager.fillReport(report, parameters, dataSource);			
		} catch (JRException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction,
					"JRException", e.getMessage()), e);
		}
	}
	
	private void outputPdf(String idTransaction, HttpServletResponse response, String fileName){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		fileName = fileName.toLowerCase().contains(".pdf") ? fileName : fileName + ".pdf";
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-pdf");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);// set HTTP/1.0 no-cache
		response.setHeader("Expires", "0");
		
		try {			
			OutputStream outStream;
			outStream = response.getOutputStream();

			JasperExportManager.exportReportToPdfStream(print, outStream);

		} catch (JRException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "JRException", e.getMessage()), e);
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		}
	}
}
