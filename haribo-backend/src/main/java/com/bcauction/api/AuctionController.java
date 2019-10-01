package com.bcauction.api;
import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IAuctionService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.AuctionInfo;
import com.bcauction.domain.Bid;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuctionController
{
	public static final Logger logger = LoggerFactory.getLogger(AuctionController.class);

	private IAuctionService auctionService;
	private IAuctionContractService auctionContractService;

	@Autowired
	public AuctionController(IAuctionService auctionService,
	                         IAuctionContractService auctionContractService) {
		Assert.notNull(auctionService, "auctionService 개체가 반드시 필요!");
		Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

		this.auctionService = auctionService;
		this.auctionContractService = auctionContractService;
	}

	@RequestMapping(value = "/auctions", method = RequestMethod.POST)
	public Auction create(@RequestBody Auction auction) {
		Auction auc = auctionService.create(auction);
		if( auc == null )
			throw new ApplicationException("auction 정보를 입력할 수 없습니다!");

		return auc;
	}

	@RequestMapping(value = "/auctions", method = RequestMethod.GET)
	public List<Auction> checkList() {
		List<Auction> list = auctionService.searchAuctionList();

		if (list == null || list.isEmpty() )
			throw new EmptyListException("NO DATA");

		return list;
	}

	@RequestMapping(value = "/auctions/{id}", method = RequestMethod.GET)
	public AuctionInfo search(@PathVariable long id) {
		Auction auction = this.auctionService.search(id);
		if (auction == null){
			logger.error("NOT FOUND AUCTION: ", id);
			throw new NotFoundException(id + " 해당 auction를 찾을 수 없습니다.");
		}

		AuctionInfo auction_info = this.auctionContractService.searchAuctionInfo(auction.getContract_address());
		if(auction_info == null){
			throw new NotFoundException(id + " 해당 auction 컨트랙트를 찾을 수 없습니다.");
		}
		auction_info.setStart_date(auction.getStart_date());
		auction_info.setEnd_date(auction.getEnd_date());
		System.out.println(auction_info);

		return auction_info;
	}

	@RequestMapping(value = "/auctions/{aid}/by/{mid}", method = RequestMethod.DELETE)
	public Auction auction취소(@PathVariable long aid, @PathVariable long mid) {
		System.out.println("들어는 온다2");
		return auctionService.auctionCancel(aid, mid);
	}

	@RequestMapping(value = "/auctions/{aid}/by/{mid}", method = RequestMethod.PUT)
	public Auction auction종료(@PathVariable long aid, @PathVariable long mid) { //mid = 최고가 입찰자 id
		System.out.println("들어는 온다");
		return this.auctionService.auctionEnd(aid, mid);
	}

	@RequestMapping(value = "/auctions/bid", method = RequestMethod.PUT)
	public Bid 입찰(@RequestBody Bid bid) {
		return auctionService.bid(bid);
	}

	/**
	 * 협업과제
	 * 협업과제
	 * week. 4-7
	 * mission. 3
	 * Req. 1-2
	 */
	@RequestMapping(value = "/auctions/owner/{id}", method = RequestMethod.GET)
	public List<Auction> memberAuctionListSearch(@PathVariable int id){
		return auctionService.searchByOwner(id);
	}

}
