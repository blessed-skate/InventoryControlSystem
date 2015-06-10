package mx.com.icsp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.AssetResponse;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.persistence.dao.AssetDao;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;

public class AssetServiceImpl implements AssetService{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	AssetDao assetDao;
	public void setAssetDao(AssetDao assetDao){
		this.assetDao = assetDao;
	}

	@Override
	public Asset[] getAsset(String idTransaction){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		List<Asset> list = null;
		Asset[] assetArray = null;
		
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			list = assetDao.getAsset(params);
			assetArray = list.toArray(new Asset[list.size()]);
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return assetArray;
	}
	
	@Override
	public int insertAsset(String idTransaction, Asset asset){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		int reponseCode = -1;
		try{
			reponseCode = assetDao.insertAsset(asset);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}
	
	@Override
	public AssetResponse insertAsset(String idTransaction, Asset[] assetArray){
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		AssetResponse response = new AssetResponse();
		
		int reponseCode = -1;
		int cont = 0;
		
		if(assetArray != null && assetArray.length > 0){
			for(Asset asset: assetArray){			
				try{
					reponseCode = assetDao.insertAsset(asset);
					log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
					if(reponseCode == 1){
						cont++;
					}else{
						sb2.append("No se inserto el registro con la etiqueta ").append(asset.getTag()).append("\n");
					}
				}catch(Exception e){
					log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
				}
			}
			sb1.append("Se insertaron ").append(cont).append(" de ").append(assetArray.length).append(" resgitros").append("\n");
			sb1.append(sb2);
		}else{
			sb1.append("No existen registros para insertar");
		}
		
		response.setResponseCode(reponseCode == 1 ? 200 : 0);
		response.setResponseMessage(sb1.toString());
		return response;
	}

	@Override
	public Asset getAssetByTag(String idTransaction, long tag) {
		
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		Asset asset = null;
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tag", tag);
			asset = assetDao.getAssetByTag(params);					
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return asset;
	}

	@Override
	public long getTag(String idTransaction, long idLedger, String idSubclass) {
		
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		long tag = -1;
		Asset asset = null;
		try{
			asset = assetDao.getTag(idLedger, idSubclass);
			tag = asset.getTag();
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return tag;
	}

	@Override
	public int updateAsset(String idTransaction, Asset asset) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		int reponseCode = -1;
		try{
			reponseCode = assetDao.updateAsset(asset);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}
}
