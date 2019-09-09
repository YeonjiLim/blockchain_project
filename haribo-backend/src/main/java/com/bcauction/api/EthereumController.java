package com.bcauction.api;

import com.bcauction.application.IAuctionContractService;
import com.bcauction.application.IEthereumService;
import com.bcauction.domain.Address;
import com.bcauction.domain.AuctionInfo;
import com.bcauction.domain.wrapper.Block;
import com.bcauction.domain.wrapper.EthereumTransaction;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/eth")
public class EthereumController {

    public static final Logger log = LoggerFactory.getLogger(EthereumController.class);

    private IEthereumService explorerService;
    private IAuctionContractService auctionContractService;

    @Autowired
    public EthereumController(IEthereumService explorerService,
                              IAuctionContractService auctionContractService) {
        Assert.notNull(explorerService, "explorerService 개체가 반드시 필요!");
        Assert.notNull(auctionContractService, "auctionContractService 개체가 반드시 필요!");

        this.explorerService = explorerService;
        this.auctionContractService = auctionContractService;
    }

    @GetMapping("/blocks")
    public List<Block> searchCurrentBlock()
    {
        List<Block> list = this.explorerService.searchCurrentBlock();

        if (list == null || list.isEmpty() )
            throw new EmptyListException("NO DATA");

        return list;
    }

    @GetMapping("/trans")
    public List<EthereumTransaction> searchCurrentTransaction()
    {
        List<EthereumTransaction> list = this.explorerService.searchCurrentTransaction();

        if (list == null || list.isEmpty() )
            throw new EmptyListException("NO DATA");

        return list;
    }

    @GetMapping("/blocks/{id}")
    public Block searchBlock(@PathVariable String id)
    {
        Block block = this.explorerService.searchBlock(id);

        if (block == null)
            throw new NotFoundException(id + " block 정보를 찾을 수 없습니다.");

        return block;
    }

    @GetMapping("/trans/{id}")
    public EthereumTransaction searchTransaction(@PathVariable String id)
    {
        EthereumTransaction transaction = this.explorerService.searchTransaction(id);

        if (transaction == null)
            throw new NotFoundException(id + " transaction 정보를 찾을 수 없습니다.");

        return transaction;
    }

    @GetMapping("/address/{addr}")
    public Address searchAddress(@PathVariable String addr)
    {
        Address address = this.explorerService.searchAddress(addr);

        if(address == null)
            throw new NotFoundException(addr + " address 정보를 찾을 수 없습니다.");

        return address;
    }

    @GetMapping("/auctions")
    public List<AuctionInfo> auctionContractAddressList(){
        List<String> auction_list = this.auctionContractService.auctionContractAddressList();

        if(auction_list == null || auction_list.isEmpty())
            throw new EmptyListException("NO DATA");

        List<AuctionInfo> auction_info_list = new ArrayList<>();
        auction_list.forEach(auction -> {
            AuctionInfo auction_info = this.auctionContractService.searchAuctionInfo(auction);
            auction_info_list.add(auction_info);
        });

        return auction_info_list;
    }

    @GetMapping("/auctions/{addr}")
    public AuctionInfo searchAuctionInfo(@PathVariable String addr){
        AuctionInfo auction_info = this.auctionContractService.searchAuctionInfo(addr);
        if(auction_info == null)
            throw new NotFoundException(addr + " auction 정보를 찾을 수 없습니다.");

        return auction_info;
    }
}
