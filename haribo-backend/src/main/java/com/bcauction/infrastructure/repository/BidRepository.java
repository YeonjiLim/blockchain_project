package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Bid;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IBidRepository;
import com.bcauction.infrastructure.repository.factory.BidFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BidRepository implements IBidRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Bid> checkList() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_bid");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{}, (rs, rowNum) -> BidFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid search(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_bid WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid search(final Bid bid)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_bid WHERE auction_participant_id=? AND auction_id=? AND bid_date=? AND bid_price=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {
										bid.getAuction_participant_id(),
										bid.getAuction_id(),
										bid.getBid_date(),
										bid.getBid_price() },
								(rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Bid search(final long auction_id, final long auction_participant_id, final BigInteger highest_price) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_bid WHERE auction_participant_id=? AND auction_id=? AND bid_price=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] {auction_participant_id, auction_id, highest_price }, (rs, rowNum) -> BidFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Bid bid) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("auction_participant_id", bid.getAuction_participant_id());
			paramMap.put("auction_id", bid.getAuction_id());
			paramMap.put("bid_date", bid.getBid_date());
			paramMap.put("bid_price", bid.getBid_price());
			paramMap.put("winning_bid", bid.getWinning_bid());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("auction_bid")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Bid bid){
		StringBuilder sbSql =  new StringBuilder("UPDATE auction_bid ");
		sbSql.append("SET winning_bid=? ");
		sbSql.append("WHERE auction_participant_id=? AND auction_id=? AND bid_price=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									bid.getWinning_bid(),
									bid.getAuction_participant_id(),
									bid.getAuction_id(),
									bid.getBid_price()
							});
		} catch (Exception e) {
			System.out.println("에러난다"+e);
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final long auction_id, final long auction_participant_id, final BigInteger bid_price) {
		StringBuilder sbSql =  new StringBuilder("UPDATE auction_bid ");
		sbSql.append("SET winning_bid=? ");
		sbSql.append("WHERE auction_id=? AND auction_participant_id=? AND bid_price=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "Y", auction_id, auction_participant_id, bid_price });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id) {
		StringBuilder sbSql =  new StringBuilder("DELETE FROM auction_bid WHERE id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(), new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}


}
