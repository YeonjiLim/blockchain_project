package com.bcauction.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Auction
{
	private long id;
	private long auction_creater_id; //회원id
	private long auction_item_id;
	private LocalDateTime creation_date;
	private String status = "V"; // V valid(유효함), C canceled, E ended
	private LocalDateTime start_date;
	private LocalDateTime end_date;
	private BigInteger lowest_price;
	private String contract_address;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public long getAuction_creater_id()
	{
		return auction_creater_id;
	}

	public void setAuction_creater_id(final long auction_creater_id)
	{
		this.auction_creater_id = auction_creater_id;
	}

	public long getAuction_item_id()
	{
		return auction_item_id;
	}

	public void setAuction_item_id(final long auction_item_id)
	{
		this.auction_item_id = auction_item_id;
	}

	public LocalDateTime getCreation_date()
	{
		return creation_date;
	}

	public void setCreation_date(final LocalDateTime creation_date)
	{
		this.creation_date = creation_date;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public LocalDateTime getStart_date()
	{
		return start_date;
	}

	public void setStart_date(final LocalDateTime start_date)
	{
		this.start_date = start_date;
	}

	public LocalDateTime getEnd_date()
	{
		return end_date;
	}

	public void setEnd_date(final LocalDateTime end_date)
	{
		this.end_date = end_date;
	}

	public BigInteger getLowest_price()
	{
		return lowest_price;
	}

	public void setLowest_price(final BigInteger lowest_price)
	{
		this.lowest_price = lowest_price;
	}

	public String getContract_address()
	{
		return contract_address;
	}

	public void setContract_address(final String contract_address)
	{
		this.contract_address = contract_address;
	}

	@Override
	public String toString() {
		return "Auction [id=" + id + ", auction_creater_id=" + auction_creater_id + ", auction_item_id="
				+ auction_item_id + ", creation_date=" + creation_date + ", status=" + status + ", start_date="
				+ start_date + ", end_date=" + end_date + ", lowest_price=" + lowest_price + ", contract_address="
				+ contract_address + "]";
	}
	
}
