package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ErrorAsyncAction extends DispatchAction{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	public void fail(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) {
	
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		int code = Integer.parseInt(request.getParameter("code") != null && !request.getParameter("code").trim().equals("") ? request.getParameter("code") : "0");
		log.info(logPattern.buildPattern(methodName, idTransaction, "code", code));
	
		StringBuilder sb = new StringBuilder();
		sb.append("location='Error.do?code=").append(code).append("'");
		
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setContentType("application/javascript");
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache
	
		PrintWriter out;
	
		try {
			out = response.getWriter();
			out.println(sb.toString());
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "IOException", e.getMessage(), sb.toString()), e);
		}
	}

}
