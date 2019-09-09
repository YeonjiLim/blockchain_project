package com.bcauction.infrastructure.repository;

import com.bcauction.domain.Transaction;
import com.bcauction.domain.exception.RepositoryException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.infrastructure.repository.factory.TransactionFactory;
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
public class TransactionRepository implements ITransactionRepository {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transaction> checkList() {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction ");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                    new Object[]{}, (rs, rowNum) -> TransactionFactory.create(rs));
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public Transaction search(String hash) {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction WHERE hash=?");
        try {
            return this.jdbcTemplate.queryForObject(sbSql.toString(),
                    new Object[] { hash }, (rs, rowNum) -> TransactionFactory.create(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public List<Transaction> searchByAddress(final String address)
    {
        StringBuilder sbSql =  new StringBuilder("SELECT * FROM transaction WHERE from_hash=? OR to_hash=?");
        try {
            return this.jdbcTemplate.query(sbSql.toString(),
                                           new Object[] { address, address }, (rs, rowNum) -> TransactionFactory.create(rs) );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }

    @Override
    public long add(Transaction transaction) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("hash", transaction.getHash());
            paramMap.put("nonce", transaction.getNonce());
            paramMap.put("block_hash", transaction.getBlockHash());
            paramMap.put("block_number", transaction.getBlockNumber());
            paramMap.put("transaction_index", transaction.getTransactionIndex());
            paramMap.put("from_hash", transaction.getFrom());
            paramMap.put("to_hash", transaction.getTo());
            paramMap.put("value", transaction.getValue());
            paramMap.put("gas_price", transaction.getGasPrice());
            paramMap.put("gas", transaction.getGas());
            paramMap.put("input", transaction.getInput());
            paramMap.put("creates", transaction.getCreates());
            paramMap.put("public_key", transaction.getPublicKey());
            paramMap.put("raw", transaction.getRaw());
            paramMap.put("r", transaction.getR());
            paramMap.put("s", transaction.getS());
            paramMap.put("v", transaction.getV());
            paramMap.put("save_date", LocalDateTime.now());

            this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("transaction")
                    .usingGeneratedKeyColumns("id");

            Number newId = simpleJdbcInsert.executeAndReturnKey(paramMap);
            return newId.longValue();

        } catch (Exception e) {
            throw new RepositoryException(e, e.getMessage());
        }
    }
}
