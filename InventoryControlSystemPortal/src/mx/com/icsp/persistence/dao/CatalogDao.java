package mx.com.icsp.persistence.dao;

import java.util.List;

import mx.com.icsc.common.AssetType;
import mx.com.icsc.common.Color;
import mx.com.icsc.common.Ledger;
import mx.com.icsc.common.Material;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

public interface CatalogDao {

	static final String SELMATERIAL = "SELECT FIIDMATERIAL ID, FCDESCRIPTION DESCRIPTION FROM cisdb.CTCISMATERIAL ORDER BY FIIDMATERIAL ASC";
	static final String SELCOLOR = "SELECT FIIDCOLOR ID, FCDESCRIPTION DESCRIPTION FROM cisdb.CTCISCOLOR ORDER BY FIIDCOLOR ASC";
	static final String SELASSETTYPE = "SELECT FCSUBCLASS ID, FCSUBCLASS SUBCLASS, FCDESCRIPTION DESCRIPTION FROM cisdb.CTCISASSETTYPE "
			+ " WHERE FIIDASSETTYPE = #{idLedger}";
	
	static final String SELLEDGER = "SELECT FIIDLEDGER ID, FCSUBCLASS IDSUBCLASS, FCDESCRIPTION DESCRIPTION FROM cisdb.CTCISLEDGER "
			+ " WHERE FIIDLEDGER = #{idLedger}";
	
	@Select(SELMATERIAL)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Material> getMaterial();

	@Select(SELCOLOR)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Color> getColor();

	@Select(SELASSETTYPE)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<AssetType> getAssetType(@Param(value = "idLedger") String idLedger);
	
	@Select(SELLEDGER)
	@Options(statementType = StatementType.CALLABLE)
	public abstract List<Ledger> getLedger(@Param(value = "idLedger") String idLedger);
}
