package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Auction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IAuctionRepository;
import com.bcauction.infrastructure.repository.factory.AuctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuctionRepository implements IAuctionRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Auction> checkList()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE status=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{ "V" }, (rs, rowNum) -> AuctionFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction search(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { id }, (rs, rowNum) -> AuctionFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Auction search(final String contract_address)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction WHERE contract_address=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { contract_address }, (rs, rowNum) -> AuctionFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Auction auction) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("auction_creater_id", auction.getAuction_creater_id());
			paramMap.put("auction_item_id", auction.getAuction_item_id());
			paramMap.put("creation_date", auction.getCreation_date());
			paramMap.put("status", auction.getStatus());
			paramMap.put("start_date", auction.getStart_date());
			paramMap.put("end_date", auction.getEnd_date());
			paramMap.put("lowest_price", auction.getLowest_price());
			paramMap.put("contract_address", auction.getContract_address());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("auction")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Auction auction)
	{
		StringBuilder sbSql =  new StringBuilder("UPDATE auction ");
		sbSql.append("SET set=? AND end_date=? ");
		sbSql.append("where id=? AND auction_creater_id=? AND auction_item_id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] {
					                           auction.getStatus(),
					                           auction.getEnd_date(),
					                           auction.getId(),
					                           auction.getAuction_creater_id(),
					                           auction.getAuction_item_id()
			                                });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("DELETE FROM auction WHERE id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
			                                new Object[] { id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
