package com.bcauction.application.impl;

import com.bcauction.application.IEthereumService;
import com.bcauction.application.IWalletService;
import com.bcauction.domain.Address;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.exception.NotFoundException;
import com.bcauction.domain.repository.IWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService implements IWalletService
{
	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	private IWalletRepository walletRepository;
	private IEthereumService ethereumService;

	@Autowired
	public WalletService(IWalletRepository walletRepository,
						 IEthereumService ethereumService) {
		this.walletRepository = walletRepository;
		this.ethereumService = ethereumService;
	}

	@Override
	public List<Wallet> checkList()
	{
		return this.walletRepository.checkList();
	}

	/**
	 * DB에 저장된 wallet_address의 정보와 이더리움의 잔액 정보를 동기화한다.
	 * @param wallet_address
	 * @return Wallet
	 */
	@Override
	public Wallet search_ETHBalanceSync(final String wallet_address)
	{
		Wallet wallet = walletRepository.search(wallet_address);
		if(wallet == null)
			throw new NotFoundException(wallet_address + " 해당 주소 wallet을 찾을 수 없습니다.");

		/**
		 * 	TODO 이더리움으로부터 잔액을 search하여
		 * 	잔액정보가 다를 경우 정보를 갱신하여 반환한다.
		 * 	ethereumService.java에 추가 메소드를 구현하는 것을 권장한다.
		 */
		
		BigDecimal bd=new BigDecimal(ethereumService.getBalance(wallet_address));
		bd=bd.divide(new BigDecimal("1000000000000000000"));
		System.out.println(bd);
		wallet.setBalance(bd);
		updateBalance(wallet_address,bd);
		return wallet;
	}

	@Override
	public Wallet search(final long id)
	{
		Wallet wallet = this.walletRepository.search(id);
		if(wallet == null)
			throw new NotFoundException(id + " 해당 회원의 주소 wallet을 찾을 수 없습니다.");
		
		return search_ETHBalanceSync(wallet.getAddress());
	}

	@Override
	public Wallet register(final Wallet wallet)
	{
		long id = this.walletRepository.add(wallet);
		return this.walletRepository.search(id);
	}

	@Override
	public Wallet updateBalance(final String wallet_address, final BigDecimal bal)
	{
		int affected = this.walletRepository.balanceUpdate(wallet_address, bal);
		if(affected == 0)
			throw new ApplicationException("잔액갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.search(wallet_address);
	}

	@Override
	public Wallet updateChargeCount(final String wallet_address)
	{
		int affected = this.walletRepository.chargingCountUpdate(wallet_address);
		if(affected == 0)
			throw new ApplicationException("충전회수갱신 처리가 반영되지 않았습니다.");

		return this.walletRepository.search(wallet_address);
	}

	/**
	 * [wallet_address]로 이더를 송금하는 충전 기능을 구현한다.
	 * 무한정 충전을 요청할 수 없도록 조건을 두어도 좋다.
	 * @param wallet_address
	 * @return Wallet
	 */
	@Override
	public Wallet charge(String wallet_address) {
		Wallet wallet = this.search_ETHBalanceSync(wallet_address);
		// System.out.print("---------------------wallet------------들어옴------------\n");
		if (wallet == null || !wallet.canCharge()) {
			throw new ApplicationException("[1] 충전할 수 없습니다!");
		}

		try {
			String txHash = this.ethereumService.charge(wallet_address);
			if(txHash == null || txHash.equals("")) {
				throw new ApplicationException("충전회수갱신 트랜잭션을 보낼 수 없습니다!");
			}
			log.info("received txhash: " + txHash);

			this.updateChargeCount(wallet_address);

			return this.search_ETHBalanceSync(wallet_address);
		}
		catch (Exception e) {
			throw new ApplicationException("[2] 충전할 수 없습니다!");
		}
	}
}
