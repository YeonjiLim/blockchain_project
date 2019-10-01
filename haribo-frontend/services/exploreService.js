var exploreService = {
  findAll: function (callback) {
    $.get(API_BASE_URL + "/api/eth/auctions/", function (data) {
      console.log(data)
      callback(data)
    })
  }
}