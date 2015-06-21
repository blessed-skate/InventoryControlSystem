package mx.com.icsp.service;

import java.util.List;

import mx.com.icsc.common.Role;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.persistence.dao.RoleDao;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;

public class RoleServiceImpl implements RoleService{
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	RoleDao roleDao;
	public void setRoleDao(RoleDao roleDao){
		this.roleDao = roleDao;
	}
	
	@Override
	public Role[] getRoles(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		List<Role> list = null;
		Role[] roleArray = null;		
		try{
			list = roleDao.getRoles();
			roleArray = list.toArray(new Role[list.size()]);
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return roleArray;
	}

	@Override
	public int insertRole(String idTransaction, Role role) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = roleDao.insertRole(role);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

}
