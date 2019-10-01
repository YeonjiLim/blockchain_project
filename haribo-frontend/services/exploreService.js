var exploreService = {
    findAllAuction : function(callback) {
        $.get(API_BASE_URL + "/api/eth/auctions/", function(data){
            console.log(data)
            callback(data)
        })
    },
    findAuctiondetail : function(contractaddress, callback) {
        $.get(API_BASE_URL + "/api/eth/auctiondetail/"+contractaddress, function(data){
            console.log(data)
            callback(data)
        })
    }
}