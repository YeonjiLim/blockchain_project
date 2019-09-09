package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Ownership;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IOwnershipRepository;
import com.bcauction.infrastructure.repository.factory.OwnershipFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OwnershipRepository implements IOwnershipRepository
{
	public static final Logger logger = LoggerFactory.getLogger(OwnershipRepository.class);

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Ownership> checkList()
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM possession_item");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{}, (rs, rowNum) -> OwnershipFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public List<Ownership> checkListByOwner(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM possession_item WHERE owner_id=?");
		return getOwnerships(id, sbSql);
	}

	@Override
	public List<Ownership> checkListByItem(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM possession_item WHERE item_id=?");
		return getOwnerships(id, sbSql);
	}

	private List<Ownership> getOwnerships(final long id, final StringBuilder sbSql)
	{
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
			                               new Object[]{id}, (rs, rowNum) -> OwnershipFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Ownership search(final long id)
	{
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM possession_item WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { (int)id }, (rs, rowNum) -> OwnershipFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public Ownership search(final long owner_id, final long item_id)
	{
		logger.info("search (owner_id, item_id) = (" + owner_id + ", " + item_id + ")");
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM possession_item WHERE owner_id=? AND item_id=?");
		try {
			Ownership o = this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { owner_id, item_id }, (rs, rowNum) -> OwnershipFactory.create(rs) );
			logger.info("" + (o!=null));
			return o;
		} catch (EmptyResultDataAccessException e) {
			logger.error("return null");
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long create(final Ownership ownership) {
		StringBuilder sbSql = new StringBuilder("INSERT INTO possession_item(owner_id,item_id,possession_start_date,possession_end_date) VALUES(?,?,?,?)");
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("owner_id", ownership.getOwner_id());
			paramMap.put("item_id", ownership.getItem_id());
			paramMap.put("possession_start_date", ownership.getPossession_start_date());
			paramMap.put("possession_end_date", ownership.getPossession_end_date());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("possession_item")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final Ownership ownership) {
		StringBuilder sbSql =  new StringBuilder("UPDATE possession_item ");
		sbSql.append("SET possession_end_date=? ");
		sbSql.append("where owner_id=? AND item_id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
							new Object[] {
									ownership.getPossession_end_date(),
									ownership.getOwner_id(),
									ownership.getItem_id()
							});
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}
}
