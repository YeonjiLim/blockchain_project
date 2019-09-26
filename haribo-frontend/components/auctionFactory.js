// Web3 Object

function createWeb3() {
  var web3 = new Web3(
    new Web3.providers.HttpProvider("http://54.180.148.38:8545")
  );
  return web3;
}

// AuctionFactory 컨트랙트 Object 생성
function createFactoryContract(web3) {
  var auctionContract = new web3.eth.Contract(
    [
      {
        constant: false,
        inputs: [
          {
            name: "workId",
            type: "uint256"
          },
          {
            name: "minValue",
            type: "uint256"
          },
          {
            name: "startTime",
            type: "uint256"
          },
          {
            name: "endTime",
            type: "uint256"
          }
        ],
        name: "createAuction",
        outputs: [
          {
            name: "",
            type: "address"
          }
        ],
        payable: false,
        stateMutability: "nonpayable",
        type: "function"
      },
      {
        constant: false,
        inputs: [
          {
            name: "newOwner",
            type: "address"
          }
        ],
        name: "transferOwnership",
        outputs: [],
        payable: false,
        stateMutability: "nonpayable",
        type: "function"
      },
      {
        inputs: [],
        payable: false,
        stateMutability: "nonpayable",
        type: "constructor"
      },
      {
        anonymous: false,
        inputs: [
          {
            indexed: false,
            name: "auctionContract",
            type: "address"
          },
          {
            indexed: false,
            name: "owner",
            type: "address"
          },
          {
            indexed: false,
            name: "numAuctions",
            type: "uint256"
          },
          {
            indexed: false,
            name: "allAuctions",
            type: "address[]"
          }
        ],
        name: "AuctionCreated",
        type: "event"
      },
      {
        anonymous: false,
        inputs: [
          {
            indexed: false,
            name: "auctionContract",
            type: "address"
          },
          {
            indexed: false,
            name: "owner",
            type: "address"
          },
          {
            indexed: false,
            name: "workId",
            type: "uint256"
          },
          {
            indexed: false,
            name: "minValue",
            type: "uint256"
          },
          {
            indexed: false,
            name: "startTime",
            type: "uint256"
          },
          {
            indexed: false,
            name: "endTime",
            type: "uint256"
          }
        ],
        name: "NewAuction",
        type: "event"
      },
      {
        anonymous: false,
        inputs: [
          {
            indexed: true,
            name: "previousOwner",
            type: "address"
          },
          {
            indexed: true,
            name: "newOwner",
            type: "address"
          }
        ],
        name: "OwnershipTransferred",
        type: "event"
      },
      {
        constant: true,
        inputs: [],
        name: "allAuctions",
        outputs: [
          {
            name: "",
            type: "address[]"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        name: "auctions",
        outputs: [
          {
            name: "",
            type: "address"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "owner",
        outputs: [
          {
            name: "",
            type: "address"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      }
    ],
    "0xF26763903Fe6014A097FC8428a1274A3952A85bf"
  );
  return auctionContract;
}

// Auction Object 컨트랙트 생성
function createAuctionContract(web3, contractAddress) {
  var auctionContract = new web3.eth.Contract(
    [
      {
        constant: true,
        inputs: [],
        name: "ended",
        outputs: [
          {
            name: "",
            type: "bool"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: false,
        inputs: [],
        name: "bid",
        outputs: [],
        payable: true,
        stateMutability: "payable",
        type: "function"
      },
      {
        constant: false,
        inputs: [],
        name: "withdraw",
        outputs: [
          {
            name: "",
            type: "bool"
          }
        ],
        payable: false,
        stateMutability: "nonpayable",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "auctionEndTime",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [
          {
            name: "_address",
            type: "address"
          }
        ],
        name: "getPendingReturnsBy",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "digitalWorkId",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "owner",
        outputs: [
          {
            name: "",
            type: "address"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: false,
        inputs: [],
        name: "cancelAuction",
        outputs: [],
        payable: false,
        stateMutability: "nonpayable",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "highestBidder",
        outputs: [
          {
            name: "",
            type: "address"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "minValue",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "getTimeLeft",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "highestBid",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "getAuctionInfo",
        outputs: [
          {
            name: "",
            type: "uint256"
          },
          {
            name: "",
            type: "uint256"
          },
          {
            name: "",
            type: "uint256"
          },
          {
            name: "",
            type: "uint256"
          },
          {
            name: "",
            type: "address"
          },
          {
            name: "",
            type: "uint256"
          },
          {
            name: "",
            type: "bool"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: true,
        inputs: [],
        name: "auctionStartTime",
        outputs: [
          {
            name: "",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "view",
        type: "function"
      },
      {
        constant: false,
        inputs: [],
        name: "endAuction",
        outputs: [],
        payable: false,
        stateMutability: "nonpayable",
        type: "function"
      },
      {
        inputs: [
          {
            name: "_owner",
            type: "address"
          },
          {
            name: "workId",
            type: "uint256"
          },
          {
            name: "minimum",
            type: "uint256"
          },
          {
            name: "startTime",
            type: "uint256"
          },
          {
            name: "endTime",
            type: "uint256"
          }
        ],
        payable: false,
        stateMutability: "nonpayable",
        type: "constructor"
      },
      {
        anonymous: false,
        inputs: [
          {
            indexed: false,
            name: "bidder",
            type: "address"
          },
          {
            indexed: false,
            name: "amount",
            type: "uint256"
          }
        ],
        name: "HighestBidIncereased",
        type: "event"
      },
      {
        anonymous: false,
        inputs: [
          {
            indexed: false,
            name: "winner",
            type: "address"
          },
          {
            indexed: false,
            name: "amount",
            type: "uint256"
          }
        ],
        name: "AuctionEnded",
        type: "event"
      }
    ],
    contractAddress
  );
  return auctionContract;
}

/**
 * TODO [경매 생성]
 * AuctionFactory의 createAuction 함수를 호출하여 경매를 생성합니다.
 * 경매 생성 시, (작품id, 최소입찰가, 경매시작시간, 경매종료시간)을 반드시 지정해야합니다.
 *  */

function createAuction(options, walletAddress, privateKey, onConfirm) {
  console.log(options);
  var web3 = createWeb3();
  // console.log(web3);
  var contract = createFactoryContract(web3);
  // console.log(contract)
  var createAuctionCall = contract.methods.createAuction(
    options.workId,
    options.minValue,
    options.startTime,
    options.endTime
  ); // 함수 호출 Object 초기화

  var encodedABI = createAuctionCall.encodeABI();
  // console.log(encodedABI);
  /**
     * 트랜잭션 생성
     *  var tx = {
        from: walletAddress,
        to: AUCTION_CONTRACT_ADDRESS,
        gas: 2000000,
        data: encodedABI
    }
     */

  console.log(walletAddress);
  var tx = {
    from: walletAddress,
    to: "0xF26763903Fe6014A097FC8428a1274A3952A85bf",
    gas: web3.utils.toHex("2000000"),
    data: encodedABI
  };

  web3.eth.accounts.signTransaction(tx, privateKey).then(function(res) {
    console.log(res);
    web3.eth
      .sendSignedTransaction(res.rawTransaction)
      .on("receipt", receipt => {
        console.log(receipt);
        var newaddress = web3.eth.abi.decodeParameters(
          ["address", "uint256", "uint256", "uint256", "uint256", "uint256"],
          receipt.logs[0].data
        );
        // console.log(newaddress);
        onConfirm(newaddress);
      });
  });

  /**
   * 트랜잭션 전자 서명 후 트랜잭션 전송/처리
   */
}

/**
 * TODO [입찰]
 * 해당 컨트랙트 주소의 bid함수를 호출하여 입찰합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */

function auction_bid(options, onConfirm) {
  var web3 = createWeb3();
  var contract = createAuctionContract(web3, options.contractAddress);
}

/**
 * TODO [경매 종료]
 * 해당 컨트랙트 주소의 endAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */

function auction_close(options, onConfirm) {}

/**
 * TODO [경매 취소]
 * 해당 컨트랙트 주소의 cancelAuction함수를 호출하여 경매를 종료합니다.
 * 경매 컨트랙트 주소: options.contractAddress
 *  */

function auction_cancel(options, onConfirm) {}
