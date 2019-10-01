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
    findCurrentTransaction : function(callback){
        $.get(API_BASE_URL + "/trans",function(data){
            console.log(data)
        })
    }
}