package com.bcauction.application;

import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.Ownership;

import java.util.List;

public interface IFabricService {
	Ownership registerPossession(long owner, long item_id);
	Ownership trensferPossession(long from, long to, long item);
	Ownership expirePossession(long owner, long item);

	List<Ownership> searchByOwner(long id);
	List<FabricAsset> searchItemHistory(long id);
}
