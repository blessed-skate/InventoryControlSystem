package mx.com.icsp.service;

import mx.com.icsc.common.Asset;
import mx.com.icsp.persistence.dao.AssetDao;

public class AssetServiceImpl implements AssetService{
	
	AssetDao assetDao;
	public void setAssetDao(AssetDao assetDao){
		this.assetDao = assetDao;
	}

	public Asset[] getAsset(){
		
		return null;
	}
}
