package com.bcauction.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid
{
	private long id;
	private long auction_participant_id;
	private long auction_id;
	private LocalDateTime bid_date;
	private BigDecimal bid_price;
	private String winning_bid = "N";
	public long getId() {
		return id;
	}
	public void setId(final long id) {
		this.id = id;
	}
	public long getAuction_participant_id() {
		return auction_participant_id;
	}
	public void setAuction_participant_id(final long auction_participant_id) {
		this.auction_participant_id = auction_participant_id;
	}
	public long getAuction_id() {
		return auction_id;
	}
	public void setAuction_id(final long auction_id) {
		this.auction_id = auction_id;
	}
	public LocalDateTime getBid_date() {
		return bid_date;
	}
	public void setBid_date(final LocalDateTime bid_date) {
		this.bid_date = bid_date;
	}
	public BigDecimal getBid_price() {
		return bid_price;
	}
	public void setBid_price(final BigDecimal bid_price) {
		this.bid_price = bid_price;
	}
	public String getWinning_bid() {
		return winning_bid;
	}
	public void setWinning_bid(final String winning_bid) {
		this.winning_bid = winning_bid;
	}

	
}
