package mx.com.icsp.persistence.dao;

import java.util.List;

import mx.com.icsc.common.Ledger;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface CatalogDao {

	static final String SELLEDGER = "SELECT FIIDLEDGER ID, FCSUBCLASS IDSUBCLASS, FCDESCRIPTION DESCRIPTION FROM ICSDB.CTICSLEDGER WHERE FIIDLEDGER = #{idLedger}";
	
	static final String SELLEDGERS = "SELECT FIIDLEDGER 'id', FCSUBCLASS 'idSubclass', IF(FCDESCRIPTION IS NOT NULL,FCDESCRIPTION, '')"
			+ " description, IF(FDREGISTERDATE IS NOT NULL, FDREGISTERDATE, CURRENT_TIMESTAMP) registerDate, IF(FDLASTUPDATE IS NOT NULL, FDLASTUPDATE, CURRENT_TIMESTAMP) lastUpdate "
			+ "FROM ICSDB.CTICSLEDGER ORDER BY 1 ASC";
	
	static final String INSLEDGER = "INSERT INTO ICSDB.CTICSLEDGER(FIIDLEDGER,FCSUBCLASS,FCDESCRIPTION) VALUES (#{id}, #{idSubclass}, #{description})";
	
	static final String UPDLEDGER = "UPDATE ICSDB.CTICSLEDGER SET FCDESCRIPTION = #{description}, FDLASTUPDATE = CURRENT_TIMESTAMP WHERE FIIDLEDGER = #{id} AND FCSUBCLASS = #{idSubclass}";

	@Select(SELLEDGER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Ledger> getLedger(@Param(value = "idLedger") String idLedger);
	
	@Select(SELLEDGERS)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Ledger> getLedgers();

	@Insert(INSLEDGER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int insertLedger(Ledger ledger);

	@Insert(UPDLEDGER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract int updateUser(Ledger ledger);
	
}