package mx.com.icsp.persistence.dao;

import java.util.List;
import java.util.Map;

import mx.com.icsc.common.Property;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface PropertyDao {

	// static final String SPCISSELUSER = "{CALL CISDB.SPCISSELUSER("
	// + "#{username, mode=IN, jdbcType=VARCHAR}"
	// + ")}";

	static final String SELPROPERTY = "select FCIDPROPERTY as 'key', FCVALUE as 'value', FCDEFAULT as 'defaultValue' from CISDB.CTCISPROPERTY";
	
	static final String INSPROPERTY = "INSERT INTO cisdb.CTCISPROPERTY (FCIDPROPERTY, FCVALUE) VALUES (#{key},#{value})";

	@Select(SELPROPERTY)
	@Options(statementType=StatementType.PREPARED)
	@MapKey(value = "key")
	public abstract Map<String, Property> getProperty();

	
	@Select(SELPROPERTY)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Property> getPropertys();

	@Insert(INSPROPERTY)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertProperty(Property property);

}
