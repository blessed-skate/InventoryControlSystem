package mx.com.icsp.action;

import mx.com.icsp.service.UserService;

import org.apache.struts.actions.DispatchAction;

public class UserAction extends DispatchAction{
	
	UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
}
