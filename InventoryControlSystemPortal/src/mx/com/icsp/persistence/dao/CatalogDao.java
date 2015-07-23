package mx.com.icsp.persistence.dao;

import java.util.List;

import mx.com.icsc.common.Ledger;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface CatalogDao {

	static final String SELLEDGER = "SELECT FIIDLEDGER ID, FCSUBCLASS IDSUBCLASS, FCDESCRIPTION DESCRIPTION FROM ICSDB.CTICSLEDGER "
			+ " WHERE FIIDLEDGER = #{idLedger}";

	@Select(SELLEDGER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Ledger> getLedger(@Param(value = "idLedger") String idLedger);
	
}
