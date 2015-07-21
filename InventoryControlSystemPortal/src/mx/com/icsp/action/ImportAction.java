package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.AssetResponse;
import mx.com.icsc.common.ImportExcelResponse;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsc.common.util.XmlFactory;
import mx.com.icsp.service.AssetService;
import mx.com.icsp.service.PropertyServiceImpl;
import mx.com.icsp.util.Constants;
import mx.com.icsp.util.CreateXML;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ImportAction extends DispatchAction {
	
	private Logger log = Logger.getLogger(this.getClass());
	private LogPattern logPattern = new LogPattern(Constants.domainCode, Constants.solutioNameCode, 
			Constants.platform, Constants.tower, this.getClass().getName());

	AssetService assetService;
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
		
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void importExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest req, HttpServletResponse res) {
		
		String idTransaction = req.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		StringBuilder sb = new StringBuilder();
		
		boolean isMultipartContent = ServletFileUpload.isMultipartContent(req);		

		if (!isMultipartContent) {
			log.info(logPattern.buildPattern(methodName, idTransaction, "You are not trying to upload..."));
			return;
		}
		
		FileItemFactory factory = null;
		ServletFileUpload upload = null;

		factory = new DiskFileItemFactory();
		upload = new ServletFileUpload(factory);
		ImportExcelResponse response = new ImportExcelResponse();
		
		try {
			List<FileItem> fields = upload.parseRequest(req);
			log.info("Number of fields: " + fields.size());
			Iterator<FileItem> it = fields.iterator();
			if (!it.hasNext()) {
				log.info(logPattern.buildPattern(methodName, idTransaction, "No fields found"));
				return;
			}
			while (it.hasNext()) {
				FileItem fileItem = it.next();
				boolean isFormField = fileItem.isFormField();
				if (isFormField) {
				} else {
					log.info(logPattern.buildPattern(methodName, idTransaction, "fileItem", ToStringBuilder.reflectionToString(fileItem)));
					boolean header = req.getParameter("header") != null && !req.getParameter("header").equals("") ? Boolean.parseBoolean(req.getParameter("header")) : Boolean.parseBoolean("false");
					int sheetNumber = Integer.parseInt(req.getParameter("sheet"));

					response.setFileName(fileItem.getName());
					response.setFileSize(fileItem.getSize());
					response.setHeader(header);
					
					Object[] params = new Object[]{header, sheetNumber};
					
					log.info(logPattern.buildPattern(methodName, idTransaction, "params", ToStringBuilder.reflectionToString(params)));
					
					try {
						if(validateWB(response)){
							if(response.getFileName().endsWith(".xls") || response.getFileName().endsWith(".xlsx")){
								Workbook wb;
								if(response.getFileName().endsWith(".xlsx"))
									wb = (XSSFWorkbook)WorkbookFactory.create(fileItem.getInputStream());
								else
									wb = (HSSFWorkbook)WorkbookFactory.create(fileItem.getInputStream());
								
								Sheet sheet = wb.getSheetAt(sheetNumber);
								
								response.setSheetName(sheet.getSheetName());
								
								List<Asset> assetList = new ArrayList<Asset>();
								
								CreateXML createXML = new CreateXML();
								createXML.createGrid(idTransaction, response, sheet, assetList);
								
								if(response.isState()){
									AssetResponse assetResponse = assetService.insertAsset(idTransaction, assetList.toArray(new Asset[assetList.size()]));
									if(assetResponse.getResponseCode() == Constants.SUCCESS_RESPONSE){
										response.setInfo(response.getInfo() +" ["+assetResponse.getResponseMessage()+"] ");
									}else{
										response.setInfo(assetResponse.getResponseMessage());
										response.setParam("");
										response.setState(false);
									}
								}
															
							}else{
								response.setInfo("La extension del archvio es invalida");
							}
						}						
//						log.error(logPattern.buildPattern(methodName, idTransaction, "importExcelResponse", ToStringBuilder.reflectionToString(response)));						
					} catch (EncryptedDocumentException e) {
						response.setInfo(e.getMessage());
						log.error(logPattern.buildPattern(methodName, idTransaction, "EncryptedDocumentException", e.getMessage()), e);
					} catch (InvalidFormatException e) {
						response.setInfo(e.getMessage());
						log.error(logPattern.buildPattern(methodName, idTransaction, "InvalidFormatException", e.getMessage()), e);
					} catch (IOException e) {
						response.setInfo(e.getMessage());
						log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
					}
				}
			}
		} catch (FileUploadException e) {
			response.setInfo(e.getMessage());
			log.error(logPattern.buildPattern(methodName, idTransaction, "FileUploadException", e.getMessage()), e);			
		}
		sb.append("{ state:").append(response.isState()).append(", name:\"").append(response.getFileName()).append("\", size:").append(response.getFileSize()).append(",");
		sb.append(" extra: { info: \"").append(response.getInfo()).append("\", param: \"").append(response.getParam()).append("\" }");
		sb.append("}");
		
		setResponse(req, res, sb.toString(), "text/json");
	}

	public void saveFile(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		AssetResponse assetResponse = new AssetResponse();
		
		try{
			String fileName = request.getParameter("fileName");
			log.info(logPattern.buildPattern(methodName, idTransaction, "fileName", fileName));
			Asset[] assetArray = (Asset[])request.getSession().getAttribute(fileName);
			assetResponse = assetService.insertAsset(idTransaction, assetArray);
		}
		catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			assetResponse.setResponseCode(200);
			assetResponse.setResponseMessage(e.getMessage());
		}
		setResponse(request, response, XmlFactory.getXml(idTransaction, assetResponse), "application/xml");		
	}	
	
	public void setResponse(HttpServletRequest request,
			HttpServletResponse response, String res, String contentType) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
	
		response.setContentType(contentType);
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//		response.setContentType("application/xml");
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache
	
		PrintWriter out;
	
		try {
			out = response.getWriter();
			out.write(res);
//			log.info(logPattern.buildPattern(methodName, idTransaction, "response", res));
		} catch (IOException e) {
			log.info(logPattern.buildPattern(methodName, idTransaction, "response", res));
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		}
	
	}

	public boolean validateWB(ImportExcelResponse response){
		int EXCEL_MAX_FILE_SIZE = Integer.parseInt(PropertyServiceImpl.map.get("EXCEL_MAX_FILE_SIZE").getValue());
		
		if((response.getFileSize()/1024) > (EXCEL_MAX_FILE_SIZE * 1024)){
			response.setInfo("El archivo es mayor a 2Mb, por favor cambie las preferencias");
			return false;
		}
		return true;
	}
}
