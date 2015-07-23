package mx.com.icsp.persistence.dao;

import java.util.List;

import mx.com.icsc.common.Role;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface RoleDao {

	// static final String SPICSSELUSER = "{CALL ICSDB.SPICSSELUSER("
	// + "#{username, mode=IN, jdbcType=VARCHAR}"
	// + ")}";


	static final String SPICSSELROLES = "SELECT FIIDROLE 'id', FCROLE role, FCDESCRIPTION description, FDREGISTERDATE registerdate, FDLASTUPDATE lastupdate FROM ICSDB.CTICSROLE";

	static final String INSROLE = "INSERT INTO ICSDB.CTICSROLE (FCROLE, FCDESCRIPTION) VALUES (#{role},#{description})";
	
	@Select(SPICSSELROLES)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Role> getRoles();
	
	@Insert(INSROLE)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertRole(Role role);

}
