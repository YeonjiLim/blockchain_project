var walletService = {
  findAddressById: function(id, callback) {
    $.get(API_BASE_URL + "/api/wallets/of/" + id, function(data) {
      callback(data["address"]);
    });
  },
  findById: function(id, callback) {
    // TODO 지갑 조회 API를 호출합니다.
    $.get(API_BASE_URL + "/api/wallets/of/" + id, function(data) {
      callback(data);
    });
  },
  isValidPrivateKey: function(id, privateKey, callback) {
    var web3 = new Web3(new Web3.providers.HttpProvider(BLOCKCHAIN_URL));
    var account = web3.eth.accounts.privateKeyToAccount(privateKey);

    this.findById(id, function(data) {
      var address = data["address"];
      callback(account && account.address == address);
    });
  },
  registerWallet: function(userId, walletAddress, callback) {
    // TODO 지갑 등록 API를 호출합니다.
    var body = {
      owner_id: userId,
      address: walletAddress
    };

    $.ajax({
      type: "POST",
      url: API_BASE_URL + "/api/wallets",
      data: JSON.stringify(body),
      headers: { "Content-Type": "application/json" },
      success: function(response) {
        callback(response);
      }
    });
  },
  chargeEther: function(walletAddress, callback) {
    // TODO 코인 충전 API를 호출합니다.
    console.log(walletAddress);
    $.ajax({
      type: "PUT",
      url: API_BASE_URL + "/api/wallets/" + walletAddress,
      data: JSON.stringify(walletAddress),
      headers: { "Content-Type": "application/json" },
      success: function(response) {
        callback(response);
      }
    });
  }
};
