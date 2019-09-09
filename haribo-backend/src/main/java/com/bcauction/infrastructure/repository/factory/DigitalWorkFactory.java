package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.DigitalWork;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DigitalWorkFactory
{
	public static DigitalWork create(ResultSet rs) throws SQLException
	{
		if (rs == null) return null;
		DigitalWork item = new DigitalWork();
		item.setId(rs.getLong("id"));
		item.setMember_id(rs.getLong("member_id"));
		item.setName(rs.getString("name"));
		item.setExplanation(rs.getString("explanation"));
		item.setStatus(rs.getString("status"));
		item.setDisclosure(rs.getString("disclosure"));

		return item;
	}
}
