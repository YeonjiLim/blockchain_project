package com.bcauction.application;

import com.bcauction.domain.FabricAsset;

import java.util.List;

public interface IFabricCCService
{
	FabricAsset registerOwnership(final long owner_id, final long item_id);
	List<FabricAsset> transferOwnership(final long from, final long to, final long item_id);
	FabricAsset expireOwnership(final long item_id, final long owner_id);
	FabricAsset query(final long item_id);
	List<FabricAsset> queryHistory(final long item_id);
}
