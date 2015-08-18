package mx.com.icsp.service;

import java.util.List;

import mx.com.icsc.common.Ledger;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.persistence.dao.CatalogDao;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;

public class CatalogServiceImpl implements CatalogService {

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());

	CatalogDao catalogDao;

	public void setCatalogDao(CatalogDao catalogDao) {
		this.catalogDao = catalogDao;
	}

	@Override
	public Ledger[] getLedger(String idTransaction, String idLedger) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Ledger[] ledgerArray = null;
		try {
			List<Ledger> list = catalogDao.getLedger(idLedger);
			ledgerArray = list.toArray(new Ledger[list.size()]);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}

		return ledgerArray;
	}

	@Override
	public Ledger[] getLedgers(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Ledger[] ledgerArray = null;
		try {
			List<Ledger> list = catalogDao.getLedgers();
			ledgerArray = list.toArray(new Ledger[list.size()]);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}

		return ledgerArray;
	}
	
	@Override
	public int insertLedger(String idTransaction, Ledger ledger) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = catalogDao.insertLedger(ledger);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

	@Override
	public int updateLedger(String idTransaction, Ledger ledger) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = catalogDao.updateUser(ledger);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}
}
