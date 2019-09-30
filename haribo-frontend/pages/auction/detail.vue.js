
var auctionDetailView = Vue.component("AuctionDetailView", {
    template: `
          <div>
              <v-nav></v-nav>
              <v-breadcrumb title="경매 작품 상세 정보" description="선택하신 경매 작품의 상세 정보를 보여줍니다."></v-breadcrumb>
              <div class="container">
                  <div class="row">
                      <div class="col-md-12">
                          <div class="card">
                              <div class="card-body">
                                  <table class="table table-bordered">
                                      <tr>
                                          <th width="20%">생성자</th>
                                          <td><router-link :to="{ name: 'work.by_user', params: { id: creator['id'] } }">{{ creator['name'] }}({{creator['email']}})</router-link></td>
                                      </tr>
                                      <tr>
                                          <th>작품명</th>
                                          <td>{{ work['name'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>작품 설명</th>
                                          <td>{{ work['explanation'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>경매 시작일</th>
                                          <td>{{ auction['start_date'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>경매 종료일</th>
                                          <td>{{ auction['end_date'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>최저가</th>
                                          <td><strong>{{ auction['lowest_price'] }} ETH</strong></td>
                                      </tr>
                                      <tr>
                                          <th>컨트랙트 주소</th>
                                          <td><a href="#">{{ auction['contract_address'] }}</a></td>
                                      </tr>
                                      <tr>
                                          <th>상태</th>
                                          <td>
                                              <span class="badge badge-success" v-if="auction['ended'] == false">경매 진행중</span>
                                              <span class="badge badge-danger" v-if="auction['ended'] == true">경매 종료</span>
                                          </td>
                                      </tr>
                                  </table>
                                  <table class="table table-bordered mt-3" v-if="bidder.id">
                                      <tr>
                                          <th width="20%">현재 최고 입찰자</th>
                                          <td>{{ bidder['name'] }}({{ bidder['email'] }})</td>
                                      </tr>
                                      <tr>
                                          <th>현재 최고 입찰액</th>
                                          <td>{{ auction['highest_bid'] }} ETH</td>
                                      </tr>
                                  </table>
                                  <div class="alert alert-warning mt-3" role="alert" v-if="!bidder.id">
                                      입찰 내역이 없습니다.
                                  </div>
                                  <div class="alert alert-danger mt-3" role="alert" v-if="auction['ended'] == true">
                                      경매가 종료되었습니다.
                                  </div>
                                  <div class="row mt-5">
                                      <div class="col-md-6">
                                          <router-link :to="{ name: 'auction' }" class="btn btn-sm btn-outline-secondary">경매 리스트로 돌아가기</router-link>
                                      </div>
                                      <div class="col-md-6 text-right" v-if="sharedStates.user.id == work['member_id'] && auction['ended'] != true">
                                          <button type="button" class="btn btn-sm btn-primary" v-on:click="closeAuction" v-bind:disabled="isCanceling || isClosing">{{ isClosing ? "낙찰중" : "낙찰하기" }}</button>
                                          <button type="button" class="btn btn-sm btn-danger" v-on:click="cancelAuction" v-bind:disabled="isCanceling || isClosing">{{ isCanceling ? "취소하는 중" : "경매취소하기" }}</button>
                                      </div>
                                      <div class="col-md-6 text-right" v-if="sharedStates.user.id != work['member_id'] && auction['ended'] != true">
                                          <router-link :to="{ name: 'auction.bid', params: { id: this.$route.params.id } }" class="btn btn-sm btn-primary">입찰하기</router-link>
                                      </div>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          </div>
      `,
    data() {
      return {
        work: {},
        creator: { id: 0 },
        auction: {},
        sharedStates: store.state,
        bidder: {},
        isCanceling: false,
        isClosing: false,
        wallet:{}
      };
    },
    methods: {
      closeAuction: function () {
        /**
         * 컨트랙트를 호출하여 경매를 종료하고
         * 경매 상태 업데이트를 위해 API를 호출합니다.
         */
        var scope = this;
        var privateKey = window.prompt(
          "경매를 종료하시려면 지갑 비밀키를 입력해주세요.",
          ""
        );
        walletService.findById(scope.sharedStates.user.id, function(wallet) {
          scope.wallet = wallet;
          console.log({"user":scope.wallet});
          var options = {
            contractAddress: scope.auction["contract_address"],
            walletAddress: scope.wallet["address"],
            privateKey: privateKey
          };
          auction_close(options, function(receipt) {
              scope.auction.ended = true;
          })
        });
        // register.vue.js, bid.vue.js를 참조하여 완성해 봅니다.
      },
      cancelAuction: function () {
        /**
         * 컨트랙트를 호출하여 경매를 취소하고
         * 경매 상태 업데이트를 위해 API를 호출합니다.
         */
        var scope = this;
        var privateKey = window.prompt(
          "경매를 취소하시려면 지갑 비밀키를 입력해주세요.",
          ""
        );
        walletService.findById(scope.sharedStates.user.id, function(wallet) {
          scope.wallet = wallet;
          console.log({"user":scope.wallet});
          var options = {
            contractAddress: scope.auction["contract_address"],
            walletAddress: scope.wallet["address"],
            privateKey: privateKey
          };
          console.log({"옵션!":options});
          auction_cancel(options, function(receipt) {
              console.log(receipt);
              scope.auction.ended = true;
          })
        });

      }
    },
    mounted: async function () {
      var auctionId = this.$route.params.id;
      var scope = this;
      var web3 = createWeb3();
  
      // 경매 정보 조회
      auctionService.findById(auctionId, function (auction) {
        var amount = Number(auction['lowest_price']).toLocaleString().split(",").join("")
        auction['lowest_price'] = web3.utils.fromWei(amount, 'ether');
        console.log({"들어옴":auction });
  
        var workId = auction['item_id'];
        // console.log(workId)
        // 작품 정보 조회
        workService.findById(workId, function (work) {
          scope.work = work;
          var creatorId = work['member_id'];
          // console.log(creatorId)
          // 생성자 정보 조회
          userService.findById(creatorId, function (user) {
            scope.creator = user;
          });
        });
        // 입찰자 조회
        if (auction['highest_bid'] > 0) {
          var amount = Number(auction['highest_bid']).toLocaleString().split(",").join("")
          auction['highest_bid'] = web3.utils.fromWei(amount, 'ether');
          var bidderId = auction['highest_bidder'];
  
          userService.findById(bidderId, function (user) {
            scope.bidder = user;
          });
        }
  
        scope.auction = auction;
      });
    }
  });