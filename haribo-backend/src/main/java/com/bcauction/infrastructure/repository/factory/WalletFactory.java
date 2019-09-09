package com.bcauction.infrastructure.repository.factory;

import com.bcauction.domain.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletFactory
{
	public static Wallet create(ResultSet rs) throws SQLException
	{
//		if (rs == null) return null;
//		Wallet 지갑 = new Wallet();
//		지갑.setId(rs.getLong("id"));
//		지갑.set소유자id(rs.getLong("소유자id"));
//		지갑.set주소(rs.getString("주소"));
//		지갑.set잔액(rs.getBigDecimal("잔액"));
//		지갑.set충전회수(rs.getInt("충전회수"));
		
		if (rs == null) return null;
		Wallet wallet = new Wallet();
		wallet.setId(rs.getLong("id"));
		wallet.setOwner_id(rs.getLong("owner_id"));
		wallet.setAddress(rs.getString("address"));
		wallet.setBalance(rs.getBigDecimal("balance"));
		wallet.setCharge_count(rs.getInt("charge_count"));

		return wallet;
	}
}
