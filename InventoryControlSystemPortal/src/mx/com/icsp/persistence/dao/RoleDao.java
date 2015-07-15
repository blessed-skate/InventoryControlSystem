package mx.com.icsp.persistence.dao;

import java.util.List;

import mx.com.icsc.common.Role;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface RoleDao {

	// static final String SPCISSELUSER = "{CALL CISDB.SPCISSELUSER("
	// + "#{username, mode=IN, jdbcType=VARCHAR}"
	// + ")}";


	static final String SPCISSELROLES = "SELECT FIIDROLE 'id', FCROLE role, FCDESCRIPTION description, FDREGISTERDATE registerdate, FDLASTUPDATE lastupdate FROM cisdb.CTCISROLE";

	static final String INSROLE = "INSERT INTO cisdb.CTCISROLE (FCROLE, FCDESCRIPTION) VALUES (#{role},#{description})";
	
	@Select(SPCISSELROLES)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Role> getRoles();
	
	@Insert(INSROLE)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertRole(Role role);

}
