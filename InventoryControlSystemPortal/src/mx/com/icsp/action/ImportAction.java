package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.util.LogPattern;
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
		
	public void importExcel(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		FileItemFactory factory = null;
		ServletFileUpload upload = null;
		CreateXML cxml = null;
		StringBuilder sb = new StringBuilder();
		
		log.info("Peticion: " + cont++);
		
		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
//		ServletFileUpload.isMultipartContent(request.get);
		if (!isMultipartContent) {
			log.info("You are not trying to upload...");
			return;
		}

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
					String filename = fileItem.getName();
					long filesize = fileItem.getSize();
					boolean state = false;
					
					String info = "Se cargo exitosamente";
					String param = "";
					
					try {
						Workbook wb = WorkbookFactory.create(fileItem.getInputStream());
						Sheet sheet = wb.getSheetAt(0);
						int columnLength = sheet.getColumnBreaks().length;
						
						if(state = validateWB(filesize, columnLength, info)){
							CreateXML createXML = new CreateXML();
							param = createXML.createGrid(sheet);
						}
						log.info("Sheet name = " + sheet.getSheetName());
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
									
					sb.append("{\"state\":").append(state).append(",\"name\":\"").append(filename).append("\",\"size\":").append(filesize).append(",");
					sb.append(" extra: { info: \"").append(info).append("\", param: \"").append(param).append("\" }");
					sb.append("}");
				}
			}
		} catch (FileUploadException e) {
			sb.append("{\"state\":false,\"extra\":{\"info\":\"Error al cargar el archivo\",\"param\":\""+e.getMessage()+"\"}}");
			log.error(logPattern.buildPattern(methodName, idTransaction, "FileUploadException", e.getMessage()), e);			
		}
		setResponse(request, response, sb);
	}
		
	public void setResponse(HttpServletRequest request,
			HttpServletResponse response, StringBuilder sb) {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();

		response.setContentType("text/json");

		PrintWriter out;

		try {
			out = response.getWriter();
			out.println(sb.toString());
			log.info(logPattern.buildPattern(methodName, idTransaction, "Response", sb.toString()));
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
