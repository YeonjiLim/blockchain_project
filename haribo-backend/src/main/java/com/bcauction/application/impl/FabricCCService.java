package com.bcauction.application.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.json.JsonObject;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bcauction.application.IFabricCCService;
import com.bcauction.domain.CommonUtil;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.FabricUser;
@Service
public class FabricCCService implements IFabricCCService
{
	private static final Logger logger = LoggerFactory.getLogger(FabricCCService.class);

	private HFClient hfClient;
	private Channel channel;

	/**
	 * 패브릭 네트워크를 이용하기 위한 정보
	 */
	@Value("${fabric.ca-server.url}")
	private String CA_SERVER_URL;
	@Value("${fabric.ca-server.admin.name}")
	private String CA_SERVER_ADMIN_NAME;
	@Value("${fabric.ca-server.pem.file}")
	private String CA_SERVER_PEM_FILE;
	@Value("${fabric.org.name}")
	private String ORG_NAME;
	@Value("${fabric.org.msp.name}")
	private String ORG_MSP_NAME;
	@Value("${fabric.org.admin.name}")
	private String ORG_ADMIN_NAME;
	@Value("${fabric.peer.name}")
	private String PEER_NAME;
	@Value("${fabric.peer.url}")
	private String PEER_URL;
	@Value("${fabric.peer.pem.file}")
	private String PEER_PEM_FILE;
	@Value("${fabric.orderer.name}")
	private String ORDERER_NAME;
	@Value("${fabric.orderer.url}")
	private String ORDERER_URL;
	@Value("${fabric.orderer.pem.file}")
	private String ORDERER_PEM_FILE;
	@Value("${fabric.org.user.name}")
	private String USER_NAME;
	@Value("${fabric.org.user.secret}")
	private String USER_SECRET;
	@Value("${fabric.channel.name}")
	private String CHANNEL_NAME;


	/**
	 * 체인코드를 이용하기 위하여
	 * 구축해놓은 패브릭 네트워크의 채널을 가져오는
	 * 기능을 구현한다.
	 * 여기에서 this.channel의 값을 초기화 한다
	 */
	private void loadChannel(){

		try {
			HFCAClient hfcaClient = HFCAClient.createNewInstance(CA_SERVER_ADMIN_NAME, CA_SERVER_URL, getPropertiesWith(CA_SERVER_PEM_FILE));
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			hfcaClient.setCryptoSuite(cryptoSuite);
			
			FabricUser adminUser=new FabricUser();
			adminUser.setName(CA_SERVER_ADMIN_NAME); // admin username
			adminUser.setAffiliation(ORG_NAME); // affiliation
			adminUser.setMspId(ORG_MSP_NAME); // org1 mspid
			Enrollment adminEnrollment = hfcaClient.enroll(CA_SERVER_ADMIN_NAME, "adminpw"); //pass admin username and password
			adminUser.setEnrollment(adminEnrollment);
			//System.out.println(CA_SERVER_ADMIN_NAME+"ASFASFBFBDFB");
			//CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			hfClient = HFClient.createNewInstance();
			hfClient.setCryptoSuite(cryptoSuite);
			hfClient.setUserContext(adminUser);

//			FabricUser fabricUser = new FabricUser();
//			fabricUser.setName(USER_NAME);
//			fabricUser.setAffiliation("org1");
//			fabricUser.setMspId("org1");
//
//			RegistrationRequest rr = new RegistrationRequest("user1", "org1");
//			String enrollmentSecret = hfcaClient.register(rr, fabricUser);
			
			
			Peer peer = hfClient.newPeer(PEER_NAME, PEER_URL, getPropertiesWith(PEER_PEM_FILE));
			//EventHub eventHub = hfClient.newEventHub(PEER_NAME, PEER_URL, getPropertiesWith(PEER_PEM_FILE));
			Orderer orderer = hfClient.newOrderer(ORDERER_NAME, ORDERER_URL, getPropertiesWith(ORDERER_PEM_FILE));
			
			 channel = hfClient.newChannel(CHANNEL_NAME);
			 channel.addPeer(peer);
			 //channel.addEventHub(eventHub);
			 channel.addOrderer(orderer);
			 channel.initialize();
			//CommonUtil.

		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	private Properties getPropertiesWith(String filename) {
		Properties properties = new Properties();
		properties.put("pemBytes", CommonUtil.readString(filename).getBytes());
		properties.setProperty("sslProvider", "openSSL");
		properties.setProperty("negotiationType", "TLS");
		return properties;
	}

	/**
	 * 소유권 등록을 위해 체인코드 함수를 차례대로 호출한다.
	 * @param owner
	 * @param item_id
	 * @return FabricAsset
	 */
	@Override
	public FabricAsset registerOwnership(final long owner, final long item_id){
		if(this.channel == null)
			loadChannel();
		
		boolean res = registerAsset(item_id, owner);
		if(!res)
			return null;
		res = confirmTimestamp(item_id);
		if(!res)
			return null;
		//queryHistory(item_id);
		System.out.println("owner 값 : =-=----->> : " + owner + "item_id 값 : ----------> " + item_id + " res 값 : -------->" + res + "\n");
		return query(item_id);
	}

	/**
	 * 소유권 이전을 위해 체인코드 함수를 차례대로 호출한다.
	 * @param from
	 * @param to
	 * @param item_id
	 * @return List<FabricAsset
	 */
	@Override
	public List<FabricAsset> transferOwnership(final long from, final long to, final long item_id) {
		if(this.channel == null)
			loadChannel();

		List<FabricAsset> assets = new ArrayList<>();
		boolean res = this.expireAssetOwnership(item_id, from);
		if(!res) return null;
		FabricAsset expired = query(item_id);
		if(expired == null) return null;
		assets.add(expired);

		res = this.updateAssetOwnership(item_id, to);
		if(!res) return null;
		FabricAsset transferred = query(item_id);
		if(transferred == null) return null;
		assets.add(transferred);

		return assets;
	}

	/**
	 * 소유권 소멸을 위해 체인코드 함수를 호출한다.
	 * @param item_id
	 * @param owner_id
	 * @return FabricAsset
	 */
	@Override
	public FabricAsset expireOwnership(final long item_id, final long owner_id) {
		if(this.channel == null)
			loadChannel();

		boolean res = this.expireAssetOwnership(item_id, owner_id);
		if(!res) return null;

		return query(item_id);
	}

	/**
	 * 체인코드 registerAsset 함수를 호출하는 메소드
	 * @param item_id
	 * @param owner
	 * @return boolean
	 */
	private boolean registerAsset(final long item_id, final long owner) {
		//QueryByChaincodeRequest qpr =hfClient.newQueryProposalRequest();
		 TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
			
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        String stringResponse = null;
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("registerAsset");
        String[] arguments={item_id+"",owner+""};
        qpr.setArgs(arguments);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 체인코드 confirmTimestamp 함수를 호출하는 메소드
	 * @param item_id
	 * @return
	 */
	private boolean confirmTimestamp(final long item_id){
		// TODO
		TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        String stringResponse = null;
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("confirmTimestamp");
        String[] arguments={item_id+""};
        qpr.setArgs(arguments);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("들어와요");
		return true;
	}

	/**
	 * 체인코드 expireAssetOwnership를 호출하는 메소드
	 * @param item_id
	 * @param owner
	 * @return
	 */
	private boolean expireAssetOwnership(final long item_id, final long owner) {
		// TODO
		TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        String stringResponse = null;
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("expireAssetOwnership");
        String[] arguments={item_id+"",owner+""};
        qpr.setArgs(arguments);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 체인코드 updateAssetOwnership를 호출하는 메소드
	 * @param item_id
	 * @param to
	 * @return
	 */
	private boolean updateAssetOwnership(final long item_id, final long to) {
		TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        String stringResponse = null;
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("updateAssetOwnership");
        String[] arguments={item_id+"",to+""};
        qpr.setArgs(arguments);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 체인코드 queryHistory 함수를 호출하는 메소드
	 * @param item_id
	 * @return
	 */
	@Override
	public List<FabricAsset> queryHistory(final long item_id){
		if(this.hfClient == null || this.channel == null)
			loadChannel();
		//getAssetHistory
		QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
		ChaincodeID ccid = ChaincodeID.newBuilder().setName("asset").build();
		queryRequest.setChaincodeID(ccid); // ChaincodeId object as created in Invoke block
		queryRequest.setFcn("getAssetHistory"); // Chaincode function name for querying the blocks

		String[] arguments = { item_id+""}; // Arguments that the above functions take
		if (arguments != null)
		 queryRequest.setArgs(arguments);

		// Query the chaincode  
		Collection<ProposalResponse> queryResponse = null;
		try {
			queryResponse = channel.queryByChaincode(queryRequest);
		} catch (InvalidArgumentException | ProposalException e) {
			e.printStackTrace();
		}
		System.out.println("ㅎㅇ2");
		for (ProposalResponse pres : queryResponse) {
		 // process the response here
			//System.out.println(pres.get);
			//private String assetId;
			//private String owner;
			//private LocalDateTime createdAt;
			//private LocalDateTime expiredAt;

		}
		
		return null;
	}
	

	/**
	 * 체인코드 query 함수를 호출하는 메소드
	 * @param item_id
	 * @return
	 */
	@Override
	public FabricAsset query(final long item_id){
		if(this.hfClient == null || this.channel == null)
			loadChannel();
		
		QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
		ChaincodeID ccid = ChaincodeID.newBuilder().setName("asset").build();
		queryRequest.setChaincodeID(ccid); // ChaincodeId object as created in Invoke block
		queryRequest.setFcn("query"); // Chaincode function name for querying the blocks

		String[] arguments = { item_id+""}; // Arguments that the above functions take
		if (arguments != null)
		 queryRequest.setArgs(arguments);

		// Query the chaincode  
		Collection<ProposalResponse> queryResponse = null;
		try {
			queryResponse = channel.queryByChaincode(queryRequest);
		} catch (InvalidArgumentException | ProposalException e) {
			e.printStackTrace();
		}
		System.out.println("ㅎㅇ");
		for (ProposalResponse pres : queryResponse) {
			
			
		 // process the response here
			//System.out.println(pres.get);
			//private String assetId;
			//private String owner;
			//private LocalDateTime createdAt;
			//private LocalDateTime expiredAt;

			
		}
		
		return null;
	}

	private static FabricAsset getAssetRecord(final JsonObject rec)
	{
		FabricAsset asset = new FabricAsset();

		
		asset.setAssetId(rec.getString("assetID"));
		asset.setOwner(rec.getString("owner"));
		asset.setCreatedAt(rec.getString("createdAt"));
		asset.setExpiredAt(rec.getString("expiredAt"));

		logger.info("Work " + rec.getString("assetID") + " by Owner " + rec.getString("owner") + ": "+
				            rec.getString("createdAt") + " ~ " + rec.getString("expiredAt"));

		return asset;
	}

}
