package mx.com.icsp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.User;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.AssetService;
import mx.com.icsp.service.PropertyService;
import mx.com.icsp.service.UserService;
import mx.com.icsp.util.Constants;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginAction extends Action{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	AssetService assetService;
	public void setAssetService(AssetService assetService){
		this.assetService = assetService;
	}
	
	UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	PropertyService propertyService;
	public void setPropertyService(PropertyService propertyService){
		this.propertyService = propertyService;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		log.info(logPattern.buildPattern(methodName, idTransaction, "Init"));
		
		propertyService.getProperty(idTransaction);
		
		String username = (String) request.getSession().getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
		User user = userService.getUser(idTransaction, username);
		log.info(logPattern.buildPattern(methodName, idTransaction, "User", ToStringBuilder.reflectionToString(user)));
		request.getSession().setAttribute("user", user);
		return mapping.findForward("home");
	}
	
}
