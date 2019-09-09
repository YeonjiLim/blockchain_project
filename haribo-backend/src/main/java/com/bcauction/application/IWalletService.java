package com.bcauction.application;

import com.bcauction.domain.Wallet;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletService
{
	List<Wallet> checkList();
	Wallet search_ETHBalanceSync(String wallet_address);
	Wallet search(long possession_id);

	@Transactional
	Wallet register(Wallet wallet);

	@Transactional
	Wallet updateBalance(String wallet_address, BigDecimal balance);

	@Transactional
	Wallet updateChargeCount(final String wallet_address);

	@Transactional
	Wallet charge(String wallet_address);
}
