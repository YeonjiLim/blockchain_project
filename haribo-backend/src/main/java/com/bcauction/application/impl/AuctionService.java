package com.bcauction.application.impl;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.Bid;
import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.domain.repository.IBidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService implements IAuctionService
{
	public static final Logger logger = LoggerFactory.getLogger(AuctionService.class);

	private IAuctionContractService auctionContractService;
	private IFabricService fabricService;
	private IAuctionRepository auctionRepository;
	private IBidRepository bidRepository;

	@Autowired
	public AuctionService(IAuctionContractService auctionContractService,
						  IFabricService fabricService,
							IAuctionRepository auctionRepository, IBidRepository bidRepository) {
		this.auctionContractService = auctionContractService;
		this.fabricService = fabricService;
		this.auctionRepository = auctionRepository;
		this.bidRepository = bidRepository;
	}

	@Override
	public List<Auction> searchAuctionList() {
		return this.auctionRepository.checkList();
	}

	@Override
	public Auction search(final long auction_id) {
		return this.auctionRepository.search(auction_id);
	}

	@Override
	public Auction search(final String contract_address) {
		return this.auctionRepository.search(contract_address);
	}

	@Override
	public Auction create(final Auction auction) {
		if(auction.getStart_date() == null) return null;
		if(auction.getEnd_date() == null) return null;
		if(auction.getAuction_creater_id() == 0) return null;
		if(auction.getAuction_item_id() == 0) return null;
		if(auction.getContract_address() == null) return null;
		if(auction.getLowest_price() == null) return null;

		auction.setCreation_date(LocalDateTime.now());
		long id = this.auctionRepository.create(auction);

		return this.auctionRepository.search(id);
	}

	@Override
	public Bid bid(Bid bid) {
		long id = this.bidRepository.create(bid);
		return this.bidRepository.search(id);
	}

	@Override
	public Bid winningBid(final long auction_id, final long member_id, final BigInteger highest_price)
	{
		int affected = this.bidRepository.update(auction_id, member_id, highest_price);
		if(affected == 0)
			return null;

		return this.bidRepository.search(auction_id, member_id, highest_price);
	}

	/**
	 * 프론트엔드에서 스마트 컨트랙트의 auction종료(endAuction) 함수 직접 호출 후
	 * 백엔드에 auction 상태 동기화를 위해 호출되는 메소드
	 * @param auction_id
	 * @param 회원id
	 * @return Auction
	 * 1. 해당 auction의 상태가 E(ended)로 바뀌고,
	 * 2. 입찰 정보 중 최고 입찰 정보를 '낙찰'로 업데이트해야 한다.
	 * 3. 데이터베이스의 소유권정보를 업데이트 한다.
	 * 4. 패브릭 상에도 소유권 이전 정보가 추가되어야 한다.
	 * 5. 업데이트 된 auction 정보를 반환한다.
	 * */
	@Override
	public Auction auctionEnd(final long auction_id, final long member_id)
	{
		// TODO
		return null;
	}

	/**
	 * 프론트엔드에서 스마트 컨트랙트의 auction취소(cancelAuction) 함수 직접 호출 후
	 * 백엔드에 auction 상태 동기화를 위해 호출되는 메소드
	 * @param auction_id
	 * @param 회원id
	 * @return Auction
	 * 1. 해당 auction의 상태와(C,canceled) 종료일시를 업데이트 한다.
	 * 2. 입찰 정보 중 최고 입찰 정보를 '낙찰'로 업데이트해야 한다.
	 * 3. 업데이트 된 auction 정보를 반환한다.
	 * */
	@Override
	public Auction auctionCancel(final long auction_id, final long member_id)
	{
		// TODO
		return null;
	}
}
