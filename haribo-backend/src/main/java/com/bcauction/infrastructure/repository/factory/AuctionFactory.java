package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Auction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionFactory
{
	public static Auction create(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Auction auction = new Auction();

		auction.setId(rs.getInt("id"));
		auction.setAuction_creater_id(rs.getLong("auction_creater_id"));
		auction.setAuction_item_id(rs.getLong("auction_item_id"));
		auction.setCreation_date(rs.getTimestamp("creation_date").toLocalDateTime());
		auction.setStart_date(rs.getTimestamp("start_date").toLocalDateTime());
		auction.setEnd_date(rs.getTimestamp("end_date").toLocalDateTime());
		auction.setStatus(rs.getString("status"));
		auction.setLowest_price(rs.getBigDecimal("lowest_price").toBigInteger());
		auction.setContract_address(rs.getString("contract_address"));

		return auction;
	}
}
