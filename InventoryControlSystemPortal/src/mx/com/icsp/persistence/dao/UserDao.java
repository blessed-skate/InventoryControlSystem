
package mx.com.icsp.persistence.dao;

import java.util.List;
import java.util.Map;

import mx.com.icsc.common.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface UserDao {

	static final String SPICSSELUSER = "SELECT FIIDUSER 'id', fcusername username, fcauthority authority, fcenabled enabled, fcname 'name', "
			+ "fclastname lastname, fcsex sex, fdbirth birth, fdregisterdate registerdate, fdlastupdate lastupdate "
			+ "FROM ICSDB.TAICSUSER WHERE fcusername = #{username}";
	
	static final String SPICSSELUSERS = "SELECT FIIDUSER 'id', IF(fcusername IS NOT NULL, fcusername, '') username, "
			+ "CASE fcauthority WHEN 'ROLE_ADMIN' THEN 'Administrador' WHEN 'ROLE_QUERY' THEN 'Consulta' ELSE 'Usuario' END authority, "
			+ "IF(fcenabled IS NOT NULL, fcenabled, '0') enabled, IF(fcname IS NOT NULL, fcname, '') 'name', "
			+ "IF(fclastname IS NOT NULL, fclastname, '') lastname, IF(fcsex IS NOT NULL, fcsex, 'M') sex, IF(fdbirth IS NOT NULL, fdbirth, CURRENT_TIMESTAMP) birth, IF(fdregisterdate IS NOT NULL, fdregisterdate, CURRENT_TIMESTAMP) registerdate, IF(fdlastupdate IS NOT NULL, fdlastupdate, CURRENT_TIMESTAMP) lastupdate "
			+ "FROM ICSDB.TAICSUSER ORDER BY 1 ASC" ;

	static final String INSUSER = "INSERT INTO ICSDB.TAICSUSER (FCUSERNAME, FCPASSWORD, FCAUTHORITY, FCENABLED, FCNAME, FCLASTNAME, FCSEX, FDBIRTH)"
			+ "VALUES (#{username},#{password},#{authority},#{enabled},#{name},#{lastName},#{sex},#{birth})";
	
	static final String UPDUSER = "UPDATE ICSDB.TAICSUSER SET FCUSERNAME=#{username}, FCPASSWORD=#{password}, FCAUTHORITY=#{authority}, FCENABLED=#{enabled}, FCNAME=#{name}, FCLASTNAME=#{lastName},"
			+ " FCSEX=#{sex}, FDBIRTH=#{birth}, FDLASTUPDATE=CURRENT_TIMESTAMP WHERE FIIDUSER=#{id}";
			
	static final String DELUSER = "DELETE FROM ICSDB.TAICSUSER WHERE FIIDUSER=#{idUser}";
	
	@Select(SPICSSELUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract User getUser(Map<String, Object> params);
	
	@Select(SPICSSELUSERS)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<User> getUsers();
	
	@Insert(INSUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertUser(User user);

	@Insert(UPDUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int updateUser(User user);

	@Insert(DELUSER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int deleteUser(int idUser);

}
