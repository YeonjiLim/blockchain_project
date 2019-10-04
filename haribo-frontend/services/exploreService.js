var exploreService = {
    findAllAuction : function(callback) {
        $.get(API_BASE_URL + "/api/eth/auctions/", function(data){
            //console.log(data)
            callback(data)
        })
    },
    findAuction : function(contractaddress, callback) {
        $.get(API_BASE_URL + "/api/eth/auctions/"+contractaddress, function(data){
            console.log(data)
            callback(data)
        })
    },
    searchLatest20Blocks : function(callback){
      $.get(API_BASE_URL + "/api/eth/blocks", function(data){
        console.log(data)
        callback(data)
      })
    },
    searchBlockByNumber : function(number, callback) {
      $.get(API_BASE_URL + "/api/eth/blocks/" + number, function(data){
        console.log(data);
        callback(data)
      })
    },
    findCurrentTransaction : function(callback){
        $.get(API_BASE_URL + "/api/eth/trans",function(data){
            console.log(data)
            callback(data)
        })
    },
    searchBlockByHash : function(hash, callback) {
      $.get(API_BASE_URL + "/api/eth/trans/" + hash, function(data){
        console.log("최종적으로 들어왔움.",data);
        callback(data)
      })
    },
}