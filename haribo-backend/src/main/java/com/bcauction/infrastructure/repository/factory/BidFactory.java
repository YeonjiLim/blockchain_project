package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Bid;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidFactory
{
	public static Bid create(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		Bid bid = new Bid();
		bid.setId(rs.getLong("id"));
		bid.setAuction_id(rs.getLong("auction_id"));
		bid.setAuction_participant_id(rs.getLong("auction_participant_id"));
		bid.setBid_date(rs.getTimestamp("bid_date").toLocalDateTime());
		bid.setBid_price(rs.getBigDecimal("bid_price"));
		bid.setWinning_bid(rs.getString("winning_bid"));

		return bid;
	}
}
