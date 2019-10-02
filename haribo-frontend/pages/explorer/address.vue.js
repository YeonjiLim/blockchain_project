
var exploreraddressView = Vue.component("ExplorerAddressView", {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Transaction Explorer" description="블록체인에서 생성된 거래내역을 보여줍니다."></v-breadcrumb>
            <div class="container">
                <explorer-nav></explorer-nav>
                <div class="row">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th colspan="2">Auction</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <input type ="text" style="width : 88%; height : 38px">
                                    <button class = "btn btn-primary">조회</button>
                                    <router-link class="btn btn-primary" v-bind:class="{ active: path.startsWith('/explorer/auction') }" :to="{ name: 'explorer.auction'}">목록</router-link>
                                </td>
                            </tr>
                        </tbody>
                        <thead>
                            <tr>
                                <th colspan="2">Block</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <input type ="text" style="width : 88%; height : 38px">
                                    <button class = "btn btn-primary">조회</button>
                                    <router-link class="btn btn-primary" v-bind:class="{ active: path.startsWith('/explorer/block') }" :to="{ name: 'explorer.block'}">목록</router-link>
                                </td>
                            </tr>
                            <tr>
                            </tr>
                        </tbody>
                        <thead>
                            <tr>
                                <th colspan="2">Transaction</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <input type ="text" style="width : 88%; height : 38px">
                                    <button class = "btn btn-primary">조회</button>
                                    <router-link class="btn btn-primary" v-bind:class="{ active: path.startsWith('/explorer/tx') }" :to="{ name: 'explorer.tx'}">목록</router-link>
                                </td>
                            </tr>
                            <tr>
                            </tr>
                        </tbody>
                    </table>                
                </div>
            </div>  
        </div>
      `,
    data() {
      return {
        path: "",
        contracts: [],
        items: [],
        A: false,
        B: false,
        T: false
      };
    },
    methods : {
        change : function(something){
            alert(something)
            this.A = !something
        }
    },
    mounted: async function() {
      /**
       * TODO
       * 1. AuctionFactory 컨트랙트로부터 경매컨트랙트 주소 리스트를 가져옵니다.
       * 2. 각 컨트랙트 주소로부터 경매의 상태(state) 정보를 가져옵니다.
       * */
      
      this.path = this.$route.path;
        var scope = this;
        exploreService.findAllAuction(function(data){
            for (let index = 0; index < data.length; index++) {
                scope.contracts.push(data[index].contract_address)
                var HighestBid = Number(data[index].highest_bid).toLocaleString().split(",").join("")
                data[index].highest_bid = web3.utils.fromWei(HighestBid, 'ether');;
            }
            scope.items = data
            console.log(scope.contracts)
        })
      console.log("address")
    //   exploreService.findAllAuction(function(data){
    //       console.log("와쩌염")
    //   })
  
    }
  });
  