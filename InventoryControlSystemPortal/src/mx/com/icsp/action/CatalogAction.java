package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.AssetType;
import mx.com.icsc.common.Color;
import mx.com.icsc.common.Ledger;
import mx.com.icsc.common.Material;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.CatalogService;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class CatalogAction extends DispatchAction {

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	CatalogService catalogService;
	public void setCatalogService(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	String struct = "<data>" + "<item value=\"value_a\" label=\"text\"/>"
			+ "<item value=\"value_b\" label=\"new text\" selected=\"true\"/>"
			+ "</data>";

	public void getLedger(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response){
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		StringBuilder sb = new StringBuilder();
		
		String idLedger = request.getParameter("idLedger");
		
		Ledger[] ledgerArray = catalogService.getLedger(idTransaction, idLedger);
		
		if(ledgerArray != null && ledgerArray.length > 0){
			sb.append("<data>");
			sb.append("<item value=\"").append(-1).append("\" label=\"").append("Seleccionar").append("\" selected=\"true\"/>");
			for(Ledger ledger : ledgerArray){
				sb.append("<item value=\"").append(ledger.getIdSubclass()).append("\" label=\"").append(ledger.getDescription()).append("\" />");
			}
			sb.append("</data>");
		}
		
		setResponse(request, response, sb);
	}
		
	public void setResponse(HttpServletRequest request, HttpServletResponse response, StringBuilder sb){
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setContentType("application/xml");
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache

		PrintWriter out;

		try {
			out = response.getWriter();
			out.println(sb.toString());
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
}
