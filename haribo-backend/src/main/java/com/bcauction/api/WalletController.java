package com.bcauction.api;

import com.bcauction.application.IWalletService;
import com.bcauction.domain.Wallet;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class WalletController {
	public static final Logger logger = LoggerFactory.getLogger(WalletController.class);

	private IWalletService walletService;

	@Autowired
	public WalletController(IWalletService walletService) {
		Assert.notNull(walletService, "walletService 개체가 반드시 필요!");
		this.walletService = walletService;
	}

	@RequestMapping(value = "/wallets", method = RequestMethod.POST)
	public Wallet register(@Valid @RequestBody Wallet wallet) {
		logger.debug(wallet.getAddress());
		logger.debug(String.valueOf(wallet.getOwner_id()));

		this.walletService.register(wallet);
		Wallet new_wallet = walletService.search_ETHBalanceSync(wallet.getAddress());

		if(new_wallet == null)
			throw new NotFoundException(wallet.getAddress() + " 해당 주소 wallet을 찾을 수 없습니다.");

		return new_wallet;
	}

	@RequestMapping(value = "/wallets", method = RequestMethod.GET)
	public List<Wallet> checkList() {
		List<Wallet> list = walletService.checkList();

		if (list == null || list.isEmpty() )
			throw new EmptyListException("NO DATA");

		return list;
	}

	@RequestMapping(value = "/wallets/{address}", method = RequestMethod.GET)
	public Wallet search(@PathVariable String address) {
		return walletService.search_ETHBalanceSync(address);
	}

	@RequestMapping(value = "/wallets/of/{mid}", method = RequestMethod.GET)
	public Wallet searchByOwner(@PathVariable long mid) {
		Wallet wallet = this.walletService.search(mid);
		//System.out.println(wallet+"AAA");
		if(wallet == null)
			throw new EmptyListException("[소유자id] " + mid + " 해당 wallet을 찾을 수 없습니다.");
		return walletService.search_ETHBalanceSync(wallet.getAddress());
	}

	@RequestMapping(value ="/wallets/{address}", method = RequestMethod.PUT)
	public Wallet charge(@PathVariable String address){ // 테스트 가능하도록 일정 개수의 코인을 충전해준다.
		return this.walletService.charge(address);
	}
}
