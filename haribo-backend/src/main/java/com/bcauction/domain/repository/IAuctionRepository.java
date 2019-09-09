package com.bcauction.domain.repository;

import com.bcauction.domain.Auction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAuctionRepository
{
//	List<Auction> 목록조회();
//	Auction 조회(long id);
//	Auction 조회(String 컨트랙트주소);
//
//	@Transactional
//	long 생성(Auction 경매);
//
//	@Transactional
//	int 수정(Auction 경매);
//
//	@Transactional
//	int 삭제(long id);
	
	List<Auction> checkList();
	Auction search(long id);
	Auction search(String contract_address);

	@Transactional
	long create(Auction auction);

	@Transactional
	int update(Auction auction);

	@Transactional
	int delete(long id);
}
