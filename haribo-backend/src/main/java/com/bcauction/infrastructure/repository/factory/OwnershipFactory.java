package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Ownership;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OwnershipFactory
{
	public static Ownership create(ResultSet rs) throws SQLException
	{
		if(rs == null) return null;
		Ownership owner = new Ownership();

		owner.setId(rs.getLong("id"));
		owner.setOwner_id(rs.getLong("owner_id"));
		owner.setItem_id(rs.getLong("item_id"));
		if(rs.getString("possession_start_date") != null)
			owner.setPossession_start_date(LocalDateTime.parse(rs.getString("possession_start_date")));
		if(rs.getString("possession_end_date") != null)
			owner.setPossession_end_date(LocalDateTime.parse(rs.getString("possession_end_date")));

		return owner;
	}
}
