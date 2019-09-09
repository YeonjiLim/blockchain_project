// 실제 Vue 템플릿 코드 작성 부분
$(function(){
    var blockNumber = parseQueryString()['blockNumber'];
    var detailView = new Vue({
        el: '#block-detail',
        data: {
            isValid: true,
            block: {
                number: blockNumber,
                hash:'',
                timestamp:'',
                miner:'',
                noncce:'',
                difficulty:'',
                size:0,
                gasLimit:0,
                gasUsed:0
            }
        },
        mounted: function(){
            if(blockNumber) {
               // TODO 
               fetchGetBlock(blockNumber).then(res=>{
                   console.log(res);
                   this.block.hash = res['hash'];
                   var timestamp = res['timestamp'];
                    timestamp = new Date(timestamp*1000);
                    this.block.timestamp = timestamp;
                    this.block.miner = res['miner'];
                    this.block.nonce = res['nonce'];
                    this.block.difficulty = res['difficulty'];
                    this.block.size = res['size'];
                    this.block.gasLimit = res['gasLimit'];
                    this.block.gasUsed = res['gasUsed'];
               })
            } else {
                this.isValid = false;
            }
        }
    });
});