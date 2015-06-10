package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.AssetResponse;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ImportAction extends DispatchAction {
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode, Constants.solutioNameCode, 
			Constants.platform, Constants.tower, this.getClass().getName());

	
	static int cont = 0;
	
	AssetService assetService;
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
		
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void importExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		StringBuilder sb = new StringBuilder();
		
		String filename = null;
		long filesize = -1;
		boolean state = false;
		
		String info = "Se cargo exitosamente";
		String param = null;
		
		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);		

		if (!isMultipartContent) {
			log.info("You are not trying to upload...");
			return;
		}
		
		FileItemFactory factory = null;
		ServletFileUpload upload = null;

		factory = new DiskFileItemFactory();
		upload = new ServletFileUpload(factory);
		try {
			List<FileItem> fields = upload.parseRequest(request);
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
					filename = fileItem.getName();
					filesize = fileItem.getSize();
					
					String header = request.getParameter("header");
					log.info(logPattern.buildPattern(methodName, idTransaction, "header", header));
					
					int sheetNumber = Integer.parseInt(request.getParameter("sheet"));
					log.info(logPattern.buildPattern(methodName, idTransaction, "sheet", String.valueOf(sheetNumber)));
					
					try {
						if(filename.endsWith(".xls") || filename.endsWith("xlsx")){
							Workbook wb = WorkbookFactory.create(fileItem.getInputStream());
							Sheet sheet = wb.getSheetAt(0);
							
							if(state = validateWB(filesize, 0, info)){
								CreateXML createXML = new CreateXML();
								param = createXML.createGrid(sheet);							
							}
							log.info("Sheet name = " + sheet.getSheetName());
						}else{
							info = "La extension del archvio es invalida";
						}
					} catch (EncryptedDocumentException e2) {
						info = e2.getMessage();
						log.error(logPattern.buildPattern(methodName, idTransaction, "EncryptedDocumentException", e2.getMessage()), e2);
					} catch (InvalidFormatException e2) {
						info = e2.getMessage();
						log.error(logPattern.buildPattern(methodName, idTransaction, "InvalidFormatException", e2.getMessage()), e2);
					} catch (IOException e2) {
						info = e2.getMessage();
						log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e2.getMessage()), e2);
					}
				}
			}
		} catch (FileUploadException e) {
			param = e.getMessage();
			log.error(logPattern.buildPattern(methodName, idTransaction, "FileUploadException", e.getMessage()), e);			
		}
		sb.append("{state:").append(state).append(",name:\"").append(filename).append("\",size:").append(filesize).append(",");
		sb.append(" extra: { info: \"").append(info).append("\", param: \"").append(param).append("\" }");
		sb.append("}");
		
//		log.info(logPattern.buildPattern(methodName, idTransaction, "response", sb.toString()));
		
		setResponse(request, response, sb.toString(), "text/json");
	}

	public void saveFile(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
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
			log.info(logPattern.buildPattern(methodName, idTransaction, "response", res));
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage()), e);
		}
	
	}

	public boolean validateWB(long filesize, int columnLength, String msg){
		boolean state = true;
		int EXCEL_MAX_FILE_SIZE = Integer.parseInt(PropertyServiceImpl.map.get("EXCEL_MAX_FILE_SIZE").getValue());
		int EXCEL_COLUMN_LENGTH = Integer.parseInt(PropertyServiceImpl.map.get("EXCEL_COLUMN_LENGTH").getValue());
		
		if((filesize/1024) > (EXCEL_MAX_FILE_SIZE * 1024)){
			state = false;		
			msg = "El archivo es mayor a 2Mb, por favor cambie las preferencias";
		}
		else if(columnLength <= EXCEL_COLUMN_LENGTH && columnLength > 0){
			state = false;		
			msg = "El formato de excel no cumple con las columnas especificadas";
		}
		return state;
	}
}