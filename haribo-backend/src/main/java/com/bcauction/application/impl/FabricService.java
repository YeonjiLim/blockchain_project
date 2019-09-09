package com.bcauction.application.impl;

import com.bcauction.application.IFabricCCService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.domain.repository.IOwnershipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FabricService
 * 작품 ownership 이력관리를 위하여
 * FabricCCService의 함수를 호출하고
 * ownership을 관리하는 DB 테이블(Ownership)에 이를 동기화한다.
 */
@Service
public class FabricService implements IFabricService
{
	private static final Logger logger = LoggerFactory.getLogger(FabricService.class);

	@Autowired
	private IFabricCCService fabricCCService;

	private IOwnershipRepository ownershipRepository;
	private IDigitalWorkRepository digitalWorkRepository;

	@Autowired
	public FabricService(IOwnershipRepository ownershipRepository,
	                     IDigitalWorkRepository digitalWorkRepository,
	                     IFabricCCService fabricCCService) {
		this.ownershipRepository = ownershipRepository;
		this.digitalWorkRepository = digitalWorkRepository;
		this.fabricCCService = fabricCCService;
	}

	/**
	 * fabricCCService의 registerOwnership을 호출하여
	 * ownership을 등록하고
	 * DB에 이 정보를 동기화한다.
	 * @param owner
	 * @param item_id
	 * @return Ownership
	 */
	@Override
	public Ownership registerPossession(final long owner, final long item_id)
	{
		System.out.println("registerPossession 함수 실행됨!!----" + owner + " -- " + item_id);
		FabricAsset asset = this.fabricCCService.registerOwnership(owner, item_id);	// 여기 null 되는거 해결하기
		System.out.println(asset+"ggggg");
		if(asset == null) return null;
		Ownership ownership = new Ownership();
		ownership.setOwner_id(owner);
		ownership.setItem_id(item_id);
		ownership.setPossession_start_date(asset.getCreatedAt());
		long result = this.ownershipRepository.create(ownership);
		System.out.println("확인"+result);
		if(result == 0)
			return null;

		Ownership searchByOwnership = this.ownershipRepository.search(owner, item_id);
		return searchByOwnership;
	}

	/**
	 * fabricCCService의 transferOwnership을 호출하여
	 * ownership을 이전하고
	 * DB에 해당 정보를 동기화 한다.
	 * @param from
	 * @param to
	 * @param item_id
	 * @return Ownership
	 */
	@Override
	public Ownership trensferPossession(final long from, final long to, final long item_id) {
		List<FabricAsset> assets = this.fabricCCService.transferOwnership(from, to, item_id);
		if(assets == null) return null;

		Ownership expire_ownership = this.ownershipRepository.search(from, item_id);
		if(expire_ownership == null) return null;

		expire_ownership.setPossession_end_date(assets.get(0).getExpiredAt());
		long result = this.ownershipRepository.update(expire_ownership);
		if(result == 0)
			return null;

		// 작품 정보 update
		DigitalWork itemInfo = this.digitalWorkRepository.search(item_id);
		if(itemInfo.getMember_id() != from) return null;

		itemInfo.setMember_id(to);
		result = this.digitalWorkRepository.update(itemInfo);
		if(result == 0)
			return null;

		Ownership newOwnership = new Ownership();
		newOwnership.setOwner_id(to);
		newOwnership.setItem_id(item_id);
		newOwnership.setPossession_start_date(assets.get(1).getCreatedAt());

		result = this.ownershipRepository.create(newOwnership);
		if(result == 0)
			return null;

		return this.ownershipRepository.search(to, item_id);
	}

	/**
	 * fabricCCService의 expireOwnership을 호출하여
	 * ownership을 소멸하고
	 * DB에 해당 정보를 동기화 한다.
	 * @param ownerid
	 * @param item_id
	 * @return Ownership
	 */
	@Override
	public Ownership expirePossession(final long ownerid, final long item_id)
	{
		
		FabricAsset asset = this.fabricCCService.expireOwnership(item_id, ownerid);
		if(asset == null) return null;

		Ownership expireOwnership = this.ownershipRepository.search(ownerid, item_id);
		if(expireOwnership == null)
			return null;

		expireOwnership.setPossession_end_date(asset.getExpiredAt());

		long result = this.ownershipRepository.update(expireOwnership);
		if(result == 0)
			return null;

		return expireOwnership;
	}


	/**
	 * fabricCCService의 queryHistory를 호출하여
	 * 작품에 대한 모든 이력을 search하고
	 * search된 정보를 정제하여 반환한다.
	 * @param id item_id
	 * @return List<FabricAsset>
	 */
	@Override
	public List<FabricAsset> searchItemHistory(final long id){
		List<FabricAsset> history = this.fabricCCService.queryHistory(id);

		// TODO

		return null;
	}

	/**
	 * owner가 소유한 작품을 search하고
	 * 해당 내역이 유효한지 검증하여 ownership 목록을 반환한다.
	 * @param id 회원id
	 * @return List<Ownership>
	 */
	@Override
	public List<Ownership> searchByOwner(final long id)
	{
		// TODO

		return null;
	}


}
