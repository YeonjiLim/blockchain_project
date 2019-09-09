package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class AuctionInfo
{
	private String contract_address;
	private BigInteger highest_bid;
	private long item_id;
	private long highest_bidder;
	private LocalDateTime start_date;
	private LocalDateTime end_date;
	private BigInteger lowest_price;
	private boolean isEnded;

	public String getContract_address() {
		return contract_address;
	}
	public void setContract_address(final String contract_address) {
		this.contract_address = contract_address;
	}
	public BigInteger getHighest_bid() {
		return highest_bid;
	}
	public void setHighest_bid(final BigInteger highest_bid) {
		this.highest_bid = highest_bid;
	}
	public long getItem_id() {
		return item_id;
	}
	public void setItem_id(final long item_id) {
		this.item_id = item_id;
	}
	public long getHighest_bidder() {
		return highest_bidder;
	}
	public void setHighest_bidder(final long highest_bidder) {
		this.highest_bidder = highest_bidder;
	}
	public LocalDateTime getStart_date() {
		return start_date;
	}
	public void setStart_date(final LocalDateTime start_date) {
		this.start_date = start_date;
	}
	public LocalDateTime getEnd_date() {
		return end_date;
	}
	public void setEnd_date(final LocalDateTime end_date) {
		this.end_date = end_date;
	}
	public BigInteger getLowest_price() {
		return lowest_price;
	}
	public void setLowest_price(final BigInteger lowest_price) {
		this.lowest_price = lowest_price;
	}
	public boolean isEnded() {
		return isEnded;
	}
	public void setEnded(final boolean isEnded) {
		this.isEnded = isEnded;
	}

}
