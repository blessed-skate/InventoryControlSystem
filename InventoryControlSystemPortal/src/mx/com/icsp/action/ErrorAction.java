package mx.com.icsp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ErrorAction extends Action{
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		int code = Integer.parseInt(request.getParameter("code"));
		ActionErrors errors = new ActionErrors();
		switch (code){
			case 1:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.invalid.credentials"));
			break;
			default:
				errors.add("ERROR_MESSAGE", new ActionMessage("error.unknow.code"));
			break;
		}
		saveErrors(request, errors);
		return mapping.findForward("LoginError");
	}

}
