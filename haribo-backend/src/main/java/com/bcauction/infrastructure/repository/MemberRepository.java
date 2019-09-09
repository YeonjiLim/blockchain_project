package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Member;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.IMemberRepository;
import com.bcauction.infrastructure.repository.factory.MemberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberRepository implements IMemberRepository {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Member> checkList() {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_member ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> MemberFactory.create(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member search(long id) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM auction_member WHERE id=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { id }, (rs, rowNum) -> MemberFactory.create(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Member search(final String email)
    {
        StringBuilder sbSql = new StringBuilder("SELECT * FROM auction_member WHERE email=?");
        try{
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                                                    new Object[] { email }, (rs, rowNum) -> MemberFactory.create(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }


    @Override
    public long add(Member member) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("name", member.getName());
            paramMap.put("email", member.getEmail());
            paramMap.put("registration_date", LocalDateTime.now());
            paramMap.put("password", member.getPassword());

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("auction_member")
                    .usingGeneratedKeyColumns("id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int update(Member member) {
        StringBuilder sbSql =  new StringBuilder("UPDATE auction_member ");
        sbSql.append("SET name=?, email=?, password=? ");
        sbSql.append("WHERE id=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { member.getName(), member.getEmail(), member.getPassword(), member.getId() });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public int delete(long id) {
        StringBuilder sbSql =  new StringBuilder("DELETE FROM auction_member ");
        sbSql.append("WHERE id=?");
        try {
            return this.jdbcTemplate.update(sbSql.toString(),
                    new Object[] { id });
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
