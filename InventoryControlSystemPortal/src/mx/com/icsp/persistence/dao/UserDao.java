package mx.com.icsp.persistence.dao;

import java.util.List;
import java.util.Map;

import mx.com.icsc.common.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface UserDao {

	// static final String SPCISSELUSER = "{CALL CISDB.SPCISSELUSER("
	// + "#{username, mode=IN, jdbcType=VARCHAR}"
	// + ")}";

	static final String SPCISSELUSER = "SELECT FIIDUSER 'id', fcusername username, fcauthority authority, fcenabled enabled, fcname 'name', "
			+ "fclastname lastname, fcsex sex, fdbirth birth, fdregisterdate registerdate, fdlastupdate lastupdate "
			+ "FROM cisdb.TACISUSER WHERE fcusername = #{username}";
	
	static final String SPCISSELUSERS = "SELECT FIIDUSER 'id', fcusername username, fcauthority authority, fcenabled enabled, fcname 'name', "
			+ "fclastname lastname, fcsex sex, fdbirth birth, fdregisterdate registerdate, fdlastupdate lastupdate "
			+ "FROM cisdb.TACISUSER" ;

	static final String INSUSER = "INSERT INTO cisdb.TACISUSER (FCUSERNAME, FCPASSWORD, FCAUTHORITY, FCENABLED, FCNAME, FCLASTNAME, FDBIRTH)"
			+ "VALUES (#{username},#{password},#{authority},#{enabled},#{name},#{lastName},#{birth})";
	
	
	@Select(SPCISSELUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract User getUser(Map<String, Object> params);
	
	@Select(SPCISSELUSERS)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<User> getUsers();
	
	@Insert(INSUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertUser(User user);

}
