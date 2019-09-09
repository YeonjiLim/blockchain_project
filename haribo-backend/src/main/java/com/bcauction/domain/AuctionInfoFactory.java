package com.bcauction.domain;

import org.web3j.tuples.generated.Tuple7;

import java.math.BigInteger;

public class AuctionInfoFactory {

    public static AuctionInfo creation(String contract_address, long wallet_owner_Id,
            Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, Boolean> info) {
        AuctionInfo auctionInfo = new AuctionInfo();
        auctionInfo.setContract_address(contract_address);
        auctionInfo.setStart_date(CommonUtil.ETHTimestamp_Conversion(info.getValue1().longValue()));
        auctionInfo.setEnd_date(CommonUtil.ETHTimestamp_Conversion(info.getValue2().longValue()));
        auctionInfo.setLowest_price(info.getValue3());
        auctionInfo.setItem_id(info.getValue4().longValue());

        auctionInfo.setHighest_bidder(wallet_owner_Id);
        auctionInfo.setHighest_bid(info.getValue6());
        auctionInfo.setEnded(info.getValue7());

        return auctionInfo;
    }
}
