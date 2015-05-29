package mx.com.icsp.action;

import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.UserService;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;

public class UserAction extends DispatchAction{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
}
