var explorerAuctionView = Vue.component("ExplorerView", {
  template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Auction Explorer" description="블록체인에 기록된 경매내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Auction</th>
                                <th>Status</th>
                                <th>Highest Bid</th>
                                <th>Highest Bidder</th>
                                <th>Expire Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in contracts">
                                <td><router-link :to="{ name: 'explorer.auction.detail', params: { contractAddress: item } }">{{ item | truncate(15) }}</router-link></td>
                                <td>
                                    <span class="badge badge-primary" v-if="items[index] && !items[index].ended">Processing</span>
                                    <span class="badge badge-danger" v-if="items[index] && items[index].ended">Ended</span>
                                </td>
                                <td>{{ items[index] && items[index].highest_bid }} ETH</td>
                                <td>
                                    <span v-if="items[index] && items[index].highest_bid != 0">{{ items[index] && items[index].highest_bidder | truncate(15) }}</span>
                                    <span v-if="items[index] && items[index].highest_bid == 0">-</span>
                                </td>
                                <td>{{ items[index] && items[index].end_date.toLocaleString() }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `,
  data() {
    return {
      contracts: [],
      items: []
    };
  },
  mounted: async function() {
    /**
     * TODO
     * 1. AuctionFactory 컨트랙트로부터 경매컨트랙트 주소 리스트를 가져옵니다.
     * 2. 각 컨트랙트 주소로부터 경매의 상태(state) 정보를 가져옵니다.
     * */
    var scope = this;
    exploreService.findAllAuction(function(data){
        console.log(data)
        for (let index = 0; index < data.length; index++) {
            scope.contracts.push(data[index].contract_address)
            var HighestBid = Number(data[index].highest_bid).toLocaleString().split(",").join("")
            data[index].highest_bid = web3.utils.fromWei(HighestBid, 'ether');;
        }
        scope.items = data
        console.log(scope.contracts)
    })

  }
});
