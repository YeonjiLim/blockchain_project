package com.bcauction.domain.repository;

import com.bcauction.domain.Bid;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface IBidRepository
{
//	List<Bid> 목록조회();
//	Bid 조회(long id);
//	Bid 조회(Bid 입찰);
//	Bid 조회(long 경매id, long 낙찰자id, BigInteger 최고가);
//
//	@Transactional
//	long 생성(Bid 입찰);
//
//	@Transactional
//	int 수정(Bid 입찰);
//	@Transactional
//	int 수정(long 경매id, long 낙찰자id, BigInteger 입찰최고가);
//
//	@Transactional
//	int 삭제(long id);
	
	List<Bid> checkList();
	Bid search(long id);
	Bid search(Bid bid);
	Bid search(long auction_id, long bidder_id, BigInteger highest_price);

	@Transactional
	long create(Bid bid);

	@Transactional
	int update(Bid bid);
	@Transactional
	int update(long auction_id, long bidder_id, BigInteger highest_price);

	@Transactional
	int delete(long id);
}
