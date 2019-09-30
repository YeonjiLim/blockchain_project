package com.bcauction.application.impl;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.domain.Auction;
import com.bcauction.domain.AuctionInfo;
import com.bcauction.domain.AuctionInfoFactory;
import com.bcauction.domain.CommonUtil;
import com.bcauction.domain.repository.IWalletRepository;
import com.bcauction.domain.wrapper.AuctionContract;
import com.bcauction.domain.wrapper.AuctionFactoryContract;
import com.bcauction.infrastructure.repository.WalletRepository;

/**
 * AuctionContractService
 * 작성, 배포한 AuctionFactory.sol Auction.sol 스마트 컨트랙트를 이용한다.
 */
@Service 
public class AuctionContractService implements IAuctionContractService {
	private static final Logger log = LoggerFactory.getLogger(AuctionContractService.class);

	@Value("${eth.auction.factory.contract}")
	private String AUCTION_FACTORY_CONTRACT;

	@Value("${eth.admin.address}")
	private String ADMIN_ADDRESS;

	@Value("${eth.admin.wallet.filename}")
	private String WALLET_RESOURCE;

	@Value("${eth.encrypted.password}")
	private String PASSWORD;

	private AuctionFactoryContract auctionFactoryContract;
	private AuctionContract auctionContract;
	private ContractGasProvider contractGasProvider = new DefaultGasProvider();
	private Credentials credentials;


	@Autowired
	private Web3j web3j;

	private IWalletRepository walletRepository;

	@Autowired
	public AuctionContractService(IWalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 경매 정보를 조회한다.
	 * @param 컨트랙트주소
	 * @return AuctionInfo
	 * 1. web3j API를 이용하여 해당 컨트랙트주소의 스마트 컨트랙트를 로드(load)한다.
	 * 2. info의 highestBidder의 정보를 가지고 최고입찰자 회원의 id를 찾아
	 * 3. AuctionInfo의 인스턴스를 생성하여 반환한다.
	 * */
	@Override
	public AuctionInfo searchAuctionInfo(final String contract_address)
	{
		// TODO
		System.out.println("컨트랙트 어드래스 : "+contract_address);
		try {
			credentials = CommonUtil.getCredential(WALLET_RESOURCE, PASSWORD);
			auctionContract = AuctionContract.load(contract_address, web3j, credentials, contractGasProvider);
			Tuple7 t7 = auctionContract.getAuctionInfo().send();
			log.info("t7", t7.toString());
			String wallet_owner = auctionContract.highestBidder().send();
			//auctionContract.bid
			System.out.println("입찰자당:  "+wallet_owner);
			if(wallet_owner.equals("0x0000000000000000000000000000000000000000")) {
				System.out.println("입찰자가 없는걸?");
				wallet_owner ="0";
			}else {
				wallet_owner = String.valueOf(walletRepository.search(wallet_owner).getOwner_id());
			}
//			System.out.println(Long.parseLong(wallet_owner));
			AuctionInfo auctioninfo = AuctionInfoFactory.creation(contract_address, Long.parseLong(wallet_owner) , t7);
			return auctioninfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰가를 조회한다.
	 * @param 컨트랙트주소
	 * @return BigInteger 현재최고가
	 * */
	@Override
	public BigInteger currentHighestPrice(final String contract_address)
	{
		// TODO
		return BigInteger.ZERO;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 해당 경매의 현재 최고 입찰 주소를 조회한다.
	 * @param 컨트랙트주소
	 * @return String 최고 입찰한 이더리움 주소(EOA)
	 * */
	@Override
	public String currentHighestBidderAddress(final String contract_address)
	{
		// TODO
		return null;
	}

	/**
	 * 이더리움 컨트랙트 주소를 이용하여 생성된 모든 경매 컨트랙트의 주소 목록을 조회한다.
	 * @return List<String> 경매 컨트랙트의 주소 리스트
	 * */
	@Override
	public List<String> auctionContractAddressList()
	{
		// TODO
		return null;
	}
}
