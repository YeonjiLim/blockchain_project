// 실제 Vue 템플릿 코드 작성 부분
$(function(){
    var hash = parseQueryString()['hash'];
    
    var detailView = new Vue({
        el: '#tx-detail',
        data: {
            isValid: true,
            tx: {
                hash: hash,
                block:0,
                timestamp: '',
                from:'',
                to:'',
                value:0,
                gas:0,
                gasPrice:0
            }
        },
        mounted: function(){
            if(hash) {
               // TODO
               
               getTransaction(hash).then(res=>{
                   console.log(res);
                   this.tx.block=res['blockNumber'];
                   
                   fetchGetBlock(this.tx.block).then(res=>{
                       console.log(res);
                       var tmp=res['timestamp'];
                       this.tx.timestamp=new Date(tmp*1000);
                   })
                   this.tx.from = res['from'];
                   this.tx.to=res['to'];
                   this.tx.gas=res['gas'];
                   this.tx.gasPrice=res['gasPrice'];
               })
            } else {
                this.isValid = false;
            }
        }
    });
});