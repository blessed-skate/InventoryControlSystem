package mx.com.icsp.service;

import java.util.HashMap;
import java.util.Map;

import mx.com.icsc.common.User;
import mx.com.icsp.persistence.dao.UserDao;

public class UserServiceImpl implements UserService{

	UserDao userDao;
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		userDao.getUser(map);
		return (User)map.get("cur");
	}

}