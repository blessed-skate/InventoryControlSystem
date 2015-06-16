package mx.com.icsp.service;

import java.util.List;

import mx.com.icsc.common.AssetType;
import mx.com.icsc.common.Color;
import mx.com.icsc.common.Ledger;
import mx.com.icsc.common.Material;
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
	public Material[] getMaterial(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Material[] materialArray = null;
		try {
			List<Material> list = catalogDao.getMaterial();
			materialArray = list.toArray(new Material[list.size()]);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return materialArray;
	}

	@Override
	public Color[] getColor(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Color[] colorArray = null;
		try {
			List<Color> list = catalogDao.getColor();
			colorArray = list.toArray(new Color[list.size()]);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return colorArray;
	}

	@Override
	public AssetType[] getAssetType(String idTransaction, String idLedger) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		AssetType[] assetTypeArray = null;
		try {
			List<AssetType> list = catalogDao.getAssetType(idLedger);
			assetTypeArray = list.toArray(new AssetType[list.size()]);
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}

		return assetTypeArray;
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
}
