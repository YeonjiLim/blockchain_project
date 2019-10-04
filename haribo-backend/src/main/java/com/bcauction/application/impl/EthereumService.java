package com.bcauction.application.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.bcauction.application.IEthereumService;
import com.bcauction.domain.Address;
import com.bcauction.domain.CommonUtil;
import com.bcauction.domain.Transaction;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.ITransactionRepository;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;

@Service
public class EthereumService implements IEthereumService {

	private static final Logger log = LoggerFactory.getLogger(EthereumService.class);

	public static final BigInteger GAS_PRICE = BigInteger.valueOf(1L);
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21_000L);

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;
	@Value("${eth.encrypted.password}")
	private String PASSWORD;
	@Value("${eth.admin.wallet.filename}")
	private String ADMIN_WALLET_FILE;

	private ITransactionRepository transactionRepository;

	@Autowired
	private Web3j web3j;


	@Autowired
	public EthereumService(ITransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	private EthBlock.Block currentBlock(final boolean fullFetched)
	{
		try {
			EthBlock latestBlockResponse;
			latestBlockResponse
					= web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, fullFetched).sendAsync().get();
			
			return latestBlockResponse.getBlock();
		}catch (ExecutionException | InterruptedException e){
			throw new ApplicationException(e.getMessage());
		}
	}

	/**
	 * 최근 블록 조회
	 * 예) 최근 20개의 블록 조회
	 * @return List<Block>
	 */
	@Override
	public List<Block> searchCurrentBlock()
	{
		// TODO
		List<Block> blk = new ArrayList<>();
		BigInteger latestBlockNumber = currentBlock(false).getNumber();
		int subValue = 0;
		for(int index = 0; index < 20; index++) {
			BigInteger sub = new BigInteger(String.valueOf(subValue));
			blk.add(searchBlock(String.valueOf(latestBlockNumber.subtract(sub))));
			subValue++;
		}
		return blk;
	}

	/**
	 * 최근 생성된 블록에 포함된 트랜잭션 조회
	 * 이더리움 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * @return List<EthereumTransaction>
	 */
	@Override
	public List<EthereumTransaction> searchCurrentTransaction()
	{
		List<EthereumTransaction> trans_list=new ArrayList<>();
		try {
			latestBlockResponse
					= web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).sendAsync().get();
			// 최근 블럭을 찾아서
			
			trans = latestBlockResponse.getBlock().getTransactions();
			System.out.println(trans);
//			for (int i = 0; i < trans.size(); i++) {
//				trans_list.add(EthereumTransaction.convertTransaction(trans.get(i));
//			}
			return trans_list;
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trans_list;
	}

	/**
	 * 특정 블록 검색
	 * 조회한 블록을 Block으로 변환해야 한다.
	 * @param 블록No
	 * @return Block
	 */
	@Override
	public Block searchBlock(String block_number)
	{
		// TODO
		Block block=null;
		try {
			latestBlockResponse
					= web3j.ethGetBlockByNumber(DefaultBlockParameterName.valueOf(block_number), true).sendAsync().get();
			return block.fromOriginalBlock(latestBlockResponse.getBlock());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return block;
	}

	/**
	 * 특정 hash 값을 갖는 트랜잭션 검색
	 * 조회한 트랜잭션을 EthereumTransaction으로 변환해야 한다.
	 * @param 트랜잭션Hash
	 * @return EthereumTransaction
	 */
	@Override
	public EthereumTransaction searchTransaction(String transaction_hash)
	{
		try {
			latestBlockResponse
			= web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).sendAsync().get();
			latestTransResponse
					= web3j.ethGetTransactionByHash(transaction_hash);
			return trans.convertTransaction(latestTransResponse);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trans;
	}

	/**
	 * 이더리움으로부터 해당 주소의 잔액을 조회하고
	 * 동기화한 트랜잭션 테이블로부터 Address 정보의 trans 필드를 완성하여
	 * 정보를 반환한다.
	 * @param 주소
	 * @return Address
	 */
	@Override
	public Address searchAddress(String address)
	{
		return null;
	}

	/**
	 * [주소]로 시스템에서 정한 양 만큼 이더를 송금한다.
	 * 이더를 송금하는 트랜잭션을 생성, 전송한 후 결과인
	 * String형의 트랜잭션 hash 값을 반환한다.
	 * @param 주소
	 * @return String 생성된 트랜잭션의 hash 반환 (참고, TransactionReceipt)
	 */
	@Override
	public String charge(final String address) // 특정 주소로 테스트 특정 양(5Eth) 만큼 충전해준다.
	{		
		//getCredential
		Admin _web3j = Admin.build(new HttpService("http://54.180.148.38:8545"));
        PersonalUnlockAccount personalUnlockAccount;
       try {
           personalUnlockAccount =  _web3j.personalUnlockAccount(ADMIN_ADDRESS,PASSWORD).send();
           if (personalUnlockAccount.accountUnlocked()) {
               System.out.println("해제됨");
           }
           //System.out.println(ADMIN_WALLET_FILE);
           Credentials credentials1 =CommonUtil.getCredential(ADMIN_WALLET_FILE, PASSWORD);
           EthGetTransactionCount ethGetTransactionCount = _web3j.ethGetTransactionCount(
                   ADMIN_ADDRESS, DefaultBlockParameterName.LATEST).sendAsync().get();
           BigInteger nonce = ethGetTransactionCount.getTransactionCount();
           System.out.println(nonce);
           RawTransaction rawTransaction = RawTransaction.createEtherTransaction (
                   nonce, GAS_PRICE, GAS_LIMIT, address, Convert.toWei("1", Convert.Unit.ETHER).toBigInteger());
           byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials1);
           String hexValue = Numeric.toHexString(signedMessage);
           EthSendTransaction ethSendTransaction = _web3j.ethSendRawTransaction(hexValue).sendAsync().get();
           String transactionHash = ethSendTransaction.getTransactionHash();
           System.out.println("전송");
           return transactionHash;
       } catch (
               IOException | InterruptedException | ExecutionException e) {
           e.printStackTrace();
           return null;
       }
		
	}

	@Override
	public BigInteger getBalance(String address) {
		// connect to node
		Web3j web3 = Web3j.build(new HttpService("http://54.180.148.38:8545"));  // defaults to http://localhost:8545/

		// send asynchronous requests to get balance
		EthGetBalance ethGetBalance;
		try {
			ethGetBalance = web3
			  .ethGetBalance(address, DefaultBlockParameterName.LATEST)
			  .sendAsync()
			  .get();
			BigInteger wei = ethGetBalance.getBalance();
			return wei;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
