package com.bcauction.application;

import com.bcauction.domain.AuctionInfo;

import java.math.BigInteger;
import java.util.List;

public interface IAuctionContractService
{
	AuctionInfo searchAuctionInfo(String contract_address);
	BigInteger currentHighestPrice(String contract_address);
	String currentHighestBidderAddress(String contract_address);
	List<String> auctionContractAddressList();
	
//	AuctionInfo 경매정보조회(String 컨트랙트주소);
//	BigInteger 현재최고가(String 컨트랙트주소);
//	String 현재최고입찰자주소(String 컨트랙트주소);
//	List<String> 경매컨트랙트주소리스트();
}
