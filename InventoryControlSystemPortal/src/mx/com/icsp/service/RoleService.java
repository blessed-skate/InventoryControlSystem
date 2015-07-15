package mx.com.icsp.service;

import mx.com.icsc.common.Role;

public interface RoleService {

	public Role[] getRoles(String idTransaction);
	public int insertRole(String idTransaction, Role role);

}
