package mx.com.icsp.service;

import mx.com.icsc.common.User;

public interface UserService {

	public User getUser(String idTransaction, String username);
	public User[] getUsers(String idTransaction);
	public int insertUser(String idTransaction, User user);
	public int updateUser(String idTransaction, User user);
	public int deleteUser(String idTransaction, int idUser);

}
