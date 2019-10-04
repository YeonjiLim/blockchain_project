package com.bcauction.application.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.hyperledger.fabric.sdk.BlockEvent;
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
		//res= updateAssetOwnership(item_id,owner);
		if(!res)
			return null;
		
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
		try {

			Thread.sleep(3000); //1초 대기

		} catch (InterruptedException e) {

			e.printStackTrace();

		}
		return query(item_id);
	}

	/**
	 * 체인코드 registerAsset 함수를 호출하는 메소드
	 * @param item_id
	 * @param owner
	 * @return boolean
	 */
	private synchronized boolean registerAsset(final long item_id, final long owner) {
		//System.out.println(item_id+"  "+owner);
		//QueryByChaincodeRequest qpr =hfClient.newQueryProposalRequest();
		 TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
			
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        String stringResponse = null;
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("registerAsset");
        String[] arguments={(item_id+"").trim(),(owner+"").trim()};
        qpr.setArgs(arguments);
        qpr.setProposalWaitTime(3000);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
			CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses);
			BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
			if(event.getBlockEvent() != null) {
				return true;
			}
		} catch (ProposalException | InvalidArgumentException | InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		} 
		
		return false;
	}

	/**
	 * 체인코드 confirmTimestamp 함수를 호출하는 메소드
	 * @param item_id
	 * @return
	 */
	private synchronized boolean confirmTimestamp(final long item_id){
		// TODO
		TransactionProposalRequest qpr = hfClient.newTransactionProposalRequest();
        ChaincodeID fabBoardCCId = ChaincodeID.newBuilder().setName("asset").build();
        qpr.setChaincodeID(fabBoardCCId);

        qpr.setFcn("confirmTimestamp");
        String[] arguments={(item_id+"").trim()};
        qpr.setArgs(arguments);
        
        qpr.setProposalWaitTime(10000);
        Collection<ProposalResponse> responses;
		try {
			responses = channel.sendTransactionProposal(qpr);
			channel.sendTransaction(responses);
			CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses);
			BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
			if(event.getBlockEvent() != null) {
				return true;
			}
		} catch (ProposalException | InvalidArgumentException | InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		}
		return false;
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
			CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses);
			BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
			if(event.getBlockEvent() != null) {
				return true;
			}
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
			CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses);
			BlockEvent.TransactionEvent event = txFuture.get(600, TimeUnit.SECONDS);
			if(event.getBlockEvent() != null) {
				return true;
			}
		} catch (ProposalException | InvalidArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 체인코드 queryHistory 함수를 호출하는 메소드
	 * @param item_id
	 * @return
	 */
	@Override
	public List<FabricAsset> queryHistory(final long item_id){
		System.out.println("!!!!!!!!!!!!!!"+item_id);
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
		List<FabricAsset> list=new ArrayList<>();
		try {
			queryResponse = channel.queryByChaincode(queryRequest);
			JsonObject o=null;
			for (ProposalResponse pres : queryResponse) {
				String s;
				try {
					s = new String(pres.getChaincodeActionResponsePayload());
					s=s.substring(1, s.length()-1);
					System.out.println(s);
					String []sp=s.split("},");
					for (int i = 0; i < sp.length; i++) {
						if(i != sp.length-1) {
							sp[i] += "}";
						}
					}
					System.out.println(Arrays.toString(sp));
					for (int i = 0; i <sp.length; i++) {
						System.out.println(sp[i]+"BBB");
						JsonReader reader = Json.createReader(new StringReader(sp[i]));
						o = reader.readObject();
						FabricAsset fa= getAssetRecord(o);
						System.out.println(fa+"AAA");
						
					}
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				} 
			}
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
		} catch (InvalidArgumentException | ProposalException e) {
			e.printStackTrace();
		}

		return list;
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
		FabricAsset fa = new FabricAsset();
		QueryByChaincodeRequest queryRequest = hfClient.newQueryProposalRequest();
		ChaincodeID ccid = ChaincodeID.newBuilder().setName("asset").build();
		queryRequest.setChaincodeID(ccid); // ChaincodeId object as created in Invoke block
		queryRequest.setFcn("query"); // Chaincode function name for querying the blocks

		String[] arguments = {item_id+""}; // Arguments that the above functions take
		if (arguments != null) 
		 queryRequest.setArgs(arguments);
		System.out.println(queryRequest.toString());
		// Query the chaincode  
		Collection<ProposalResponse> queryResponse = null;
		try {
			queryResponse = channel.queryByChaincode(queryRequest);
			for (ProposalResponse pres : queryResponse) {
				
			}
		} catch (InvalidArgumentException | ProposalException e) {
			e.printStackTrace();
		}
		//queryHistory(item_id);
		JsonObject o=null;
		for (ProposalResponse pres : queryResponse) {
//			System.out.println(pres.getProposalResponse().getResponse().getPayload()+"BBB");
			String s;
			try {
				s = new String(pres.getChaincodeActionResponsePayload());
				//JSONObject jsonObject = new JSONObject(s);
				//jsonObject.get("");
				//JsonObject jo=new JsonObject();
				//com.google.gson.JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
				//getAssetRecord(jsonObject);
				JsonReader reader = Json.createReader(new StringReader(s));
			       o = reader.readObject();
			      //System.out.println(getAssetRecord(o));
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return getAssetRecord(o);
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
