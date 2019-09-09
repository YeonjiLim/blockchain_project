package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.infrastructure.repository.factory.WalletFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WalletRepository implements IWalletRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Wallet> checkList()
	{
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM wallet");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> WalletFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Wallet search(final long owner_id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM wallet WHERE owner_id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { owner_id }, (rs, rowNum) -> WalletFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Wallet search(final String wallet_address)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM wallet WHERE address=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { wallet_address }, (rs, rowNum) -> WalletFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long add(final Wallet wallet)
	{
		try{
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("owner_id", wallet.getOwner_id());
			paramMap.put("address", wallet.getAddress());
			paramMap.put("balance", wallet.getBalance());
			paramMap.put("charge_count", 0);

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("wallet")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		}catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int balanceUpdate(final String wallet_address,final BigDecimal balance) {
		StringBuilder sbSql =  new StringBuilder("UPDATE wallet ");
		sbSql.append("SET balance=? ");
		sbSql.append("where address=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { balance, wallet_address });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int chargingCountUpdate(final String wallet_address) {
		StringBuilder sbSql =  new StringBuilder("UPDATE wallet SET charge_count = charge_count + 1 ");
		sbSql.append("WHERE address=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { wallet_address });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
