package mx.com.icsp.service;

import mx.com.icsc.common.Asset;
import mx.com.icsc.common.AssetResponse;

public interface AssetService {

	public Asset[] getAsset(String idTransaction);
	public int insertAsset(String idTransaction, Asset asset);
	public Asset getAssetByTag(String idTransaction, long tag);
	long getTag(String idTransaction, long idLedger, String idSubclass);
	public int updateAsset(String idTransaction, Asset asset);
	public AssetResponse insertAsset(String idTransaction, Asset[] assetArray);
}
