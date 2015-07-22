package mx.com.icsp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.icsc.common.User;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.persistence.dao.UserDao;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;

public class UserServiceImpl implements UserService{

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	UserDao userDao;
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	@Override
	public User getUser(String idTransaction, String username) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		User user = null;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("username", username);
		    user = userDao.getUser(map);
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return user;
	}

	@Override
	public User[] getUsers(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		List<User> list = null;
		User[] userArray = null;
		try{
			list = userDao.getUsers();
			userArray = list.toArray(new User[list.size()]);
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return userArray;
	}

	@Override
	public int insertUser(String idTransaction, User user) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = userDao.insertUser(user);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

	@Override
	public int updateUser(String idTransaction, User user) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = userDao.updateUser(user);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

	@Override
	public int deleteUser(String idTransaction, int idUser) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = userDao.deleteUser(idUser);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

}
