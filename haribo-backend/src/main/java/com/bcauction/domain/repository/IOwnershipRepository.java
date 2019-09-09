package com.bcauction.domain.repository;

import com.bcauction.domain.Ownership;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOwnershipRepository
{
//	List<Ownership> 목록조회();
//	List<Ownership> 소유자별목록조회(long id);
//	List<Ownership> 작품별목록조회(long id);
//	Ownership 조회(long id);
//	Ownership 조회(long 소유자, long 작품id);
//
//	@Transactional
//	long 생성(Ownership 소유권);
//
//	@Transactional
//	int 수정(Ownership 소유권);
	
	List<Ownership> checkList();
	List<Ownership> checkListByOwner(long id);
	List<Ownership> checkListByItem(long id);
	Ownership search(long id);
	Ownership search(long owner, long item_id);

	@Transactional
	long create(Ownership ownerShip);

	@Transactional
	int update(Ownership ownerShip);
}
