package mx.com.icsp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.User;
import mx.com.icsp.service.AssetService;
import mx.com.icsp.service.UserService;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginAction extends Action{
	
	AssetService assetService;
	public void setAssetService(AssetService assetService){
		this.assetService = assetService;
	}
	
	UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String username = (String) request.getSession().getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
		Asset[] assetArray = assetService.getAsset();
		User user = userService.getUser(username);
		return mapping.findForward("home");
	}
	
}