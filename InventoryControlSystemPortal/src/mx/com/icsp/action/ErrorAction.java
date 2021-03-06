package mx.com.icsp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ErrorAction extends Action{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){

		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		int code = Integer.parseInt(request.getParameter("code"));
		log.info(logPattern.buildPattern(methodName, idTransaction, "code", code));
		ActionErrors errors = new ActionErrors();
		switch (code){
			case 1:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.invalid.credentials"));
			break;
			case 2:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.access.denied"));
			break;
			case 3:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.access.unauthorized"));
			break;
			case 4:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.logout"));
			break;
			case 5:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.concurrent.session"));
			break;
			
			case 401: //No se ha logeado
				errors.add("ERROR_MESSAGE", new ActionMessage("error.401"));
			break;
			case 403: //Acceso prohibido
				errors.add("ERROR_MESSAGE", new ActionMessage("error.403"));
			break;
			case 404: //La pagina a la que intenta entrar no existe
				errors.add("ERROR_MESSAGE", new ActionMessage("error.404"));
			break;
			case 500: //Error interno en el servidor
				errors.add("ERROR_MESSAGE", new ActionMessage("error.500"));
			break;
			case 503: //Error interno en el servidor
				errors.add("ERROR_MESSAGE", new ActionMessage("error.503"));
			break;
			
			default:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.unknow.code"));
			break;
		}
		saveErrors(request, errors);
		return mapping.findForward("login");
	}

}
