package com.bcauction.application;

import com.bcauction.domain.Auction;
import com.bcauction.domain.Bid;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface IAuctionService
{
	List<Auction> searchAuctionList();
	Auction search(long auction_id);
	Auction search(String contract_address);

	@Transactional
	Auction create(Auction auction);

	@Transactional
	Bid bid(Bid bid);

	@Transactional
	Bid winningBid(long auction_id,long member_id, final BigInteger highest_price);

	@Transactional
	Auction auctionEnd(long auction_id, long member_id); // 현재 최고가에서 끝내기, 소유권 이전

	@Transactional
	Auction auctionCancel(long auction_id, long member_id); // 환불 후 옥션 끝내기
}
