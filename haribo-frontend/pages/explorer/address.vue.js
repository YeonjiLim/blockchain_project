
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
                                    <input type ="text" v-model="Aaddress" style="width : 88%; height : 38px">
                                    <router-link class="btn btn-primary" :to="{ name: 'explorer.auction.detail', params: { contractAddress: Aaddress } }">조회</router-link>
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
                                    <input type ="text" v-model="Baddress" style="width : 88%; height : 38px">
                                    <router-link class="btn btn-primary" :to="{name:'explorer.block.detail', params: {blockNumber:Baddress}}">조회</router-link>
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
                                    <input type ="text" v-model="Taddress" style="width : 88%; height : 38px">
                                    <router-link class="btn btn-primary" :to="{name: 'explorer.tx.detail', params: { hash: Taddress }}">조회</router-link>
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
        Aaddress : "",
        Baddress : "",
        Taddress : ""
      };
    },
    methods : {
        search(data){
            alert(data)
        }
    },
    mounted: async function() {
      
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
  