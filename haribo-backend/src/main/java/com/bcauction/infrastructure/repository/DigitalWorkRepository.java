package com.bcauction.infrastructure.repository;

import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.infrastructure.repository.factory.DigitalWorkFactory;
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
public class DigitalWorkRepository implements IDigitalWorkRepository
{
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<DigitalWork> checkList() {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM item WHERE disclosure=? AND status=?");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", "Y"}, (rs, rowNum) -> DigitalWorkFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}


	@Override
	public List<DigitalWork> memberItemCheckList(final long member_id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM item WHERE status=? AND member_id=? ");
		try {
			return this.jdbcTemplate.query(sbSql.toString(),
							   new Object[]{"Y", member_id}, (rs, rowNum) -> DigitalWorkFactory.create(rs));
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork search(final long id) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM item WHERE id=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
								new Object[] { id }, (rs, rowNum) -> DigitalWorkFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public DigitalWork search(final long member_id, final String name) {
		StringBuilder sbSql =  new StringBuilder("SELECT * FROM item WHERE member_id=? AND name=?");
		try {
			return this.jdbcTemplate.queryForObject(sbSql.toString(),
			                                        new Object[] { member_id, name }, (rs, rowNum) -> DigitalWorkFactory.create(rs) );
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public long add(final DigitalWork item) {
		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("name", item.getName());
			paramMap.put("explanation", item.getExplanation());
			paramMap.put("disclosure", item.getDisclosure());
			paramMap.put("status", item.getStatus());
			paramMap.put("member_id", item.getMember_id());

			this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
					.withTableName("item")
					.usingGeneratedKeyColumns("id");

			Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
			return newId.longValue();

		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int update(final DigitalWork item) {
		StringBuilder sbSql =  new StringBuilder("UPDATE item ");
		sbSql.append("SET name=?, explanation=?, disclosure=?, status=?, member_id=? ");
		sbSql.append("where id=?");
		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] {
										item.getName(),
										item.getExplanation(),
										item.getDisclosure(),
										item.getStatus(),
										item.getMember_id(),
										item.getId()
			                                });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}
	}

	@Override
	public int delete(final long id) { // 상태를 N으로 업데이트
		StringBuilder sbSql =  new StringBuilder("UPDATE item ");
		sbSql.append("SET status=?, disclosure=? ");
		sbSql.append("where id=?");

		try {
			return this.jdbcTemplate.update(sbSql.toString(),
								new Object[] { "N", "N", id });
		} catch (Exception e) {
			throw new RepositoryException(e, e.getMessage());
		}

	}

}
