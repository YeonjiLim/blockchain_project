package com.bcauction.application;

import com.bcauction.domain.Address;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface IEthereumService {
    List<Block> searchCurrentBlock();
    List<EthereumTransaction> searchCurrentTransaction();

    Block searchBlock(String block_number);
    EthereumTransaction searchTransaction(String transaction_hash);

    String charge(String address);

    Address searchAddress(String address);
    BigInteger getBalance(String address);
}
