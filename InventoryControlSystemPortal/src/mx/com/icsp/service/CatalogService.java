package mx.com.icsp.service;

import mx.com.icsc.common.Ledger;

public interface CatalogService {
	
	public Ledger[] getLedger(String idTransaction, String idLedger);
	public Ledger[] getLedgers(String idTransaction);
	public int insertLedger(String idTransaction, Ledger ledger);
	public int updateLedger(String idTransaction, Ledger ledger);
	
}