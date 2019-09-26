pragma solidity ^0.5.2;


/*
 * @dev Provides information about the current execution context, including the
 * sender of the transaction and its data. While these are generally available
 * via msg.sender and msg.data, they should not be accessed in such a direct
 * manner, since when dealing with GSN meta-transactions the account sending and
 * paying for execution may not be the actual sender (as far as an application
 * is concerned).
 *
 * This contract is only required for intermediate, library-like contracts.
 */
contract Context {
    // Empty internal constructor, to prevent people from mistakenly deploying
    // an instance of this contract, which should be used via inheritance.
    constructor () internal { }
    // solhint-disable-previous-line no-empty-blocks

    function _msgSender() internal view returns (address payable) {
        return msg.sender;
    }

    function _msgData() internal view returns (bytes memory) {
        this; // silence state mutability warning without generating bytecode - see https://github.com/ethereum/solidity/issues/2691
        return msg.data;
    }
}



/**
 * @dev Contract module which provides a basic access control mechanism, where
 * there is an account (an owner) that can be granted exclusive access to
 * specific functions.
 *
 * This module is used through inheritance. It will make available the modifier
 * `onlyOwner`, which can be applied to your functions to restrict their use to
 * the owner.
 */
contract Ownable is Context {
    address private _owner;

    event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

    /**
     * @dev Initializes the contract setting the deployer as the initial owner.
     */
    constructor () internal {
        address msgSender = _msgSender();
        _owner = msgSender;
        emit OwnershipTransferred(address(0), msgSender);
    }

    /**
     * @dev Returns the address of the current owner.
     */
    function owner() public view returns (address) {
        return _owner;
    }

    /**
     * @dev Throws if called by any account other than the owner.
     */
    modifier onlyOwner() {
        require(isOwner(), "Ownable: caller is not the owner");
        _;
    }

    /**
     * @dev Returns true if the caller is the current owner.
     */
    function isOwner() public view returns (bool) {
        return _msgSender() == _owner;
    }

    /**
     * @dev Leaves the contract without owner. It will not be possible to call
     * `onlyOwner` functions anymore. Can only be called by the current owner.
     *
     * NOTE: Renouncing ownership will leave the contract without an owner,
     * thereby removing any functionality that is only available to the owner.
     */
    function renounceOwnership() public onlyOwner {
        emit OwnershipTransferred(_owner, address(0));
        _owner = address(0);
    }

    /**
     * @dev Transfers ownership of the contract to a new account (`newOwner`).
     * Can only be called by the current owner.
     */
    function transferOwnership(address newOwner) public onlyOwner {
        _transferOwnership(newOwner);
    }

    /**
     * @dev Transfers ownership of the contract to a new account (`newOwner`).
     */
    function _transferOwnership(address newOwner) internal {
        require(newOwner != address(0), "Ownable: new owner is the zero address");
        emit OwnershipTransferred(_owner, newOwner);
        _owner = newOwner;
    }
}



/// @title 경매
contract Auction is Ownable{
 address payable public beneficiary;
 uint public auctionEndTime;
 uint public minValue;
 // 현재 최고 입찰 상태
 address public highestBidder;
 uint public highestBid;
 mapping(address => uint) pendingReturns;
 address payable[] bidders;
 bool ended;
 event HighestBidIncereased(address bidder, uint amount);
 event AuctionEnded(address winner, uint amount);
 /// @notice 경매 생성
 /// @param minimum 경매품의 최소 가격
 /// @param hoursAfter 경매 진행 기간, 시간 단위
 /// @dev 생성자에서 경매의 상태 변수 beneficiary, auctionEndTime, minValue이 정해짐.
 constructor(uint minimum, uint hoursAfter) public payable{
     require(minimum > 0);
     minValue = minimum * 1 ether;
     beneficiary = msg.sender;
     auctionEndTime = now + hoursAfter * 1 hours;
 }
 /// @dev 이더를 지불하여 경매에 참가하기 위해 payable 함수로 작성
 /// 파라메터 필요하지 않음.
 /// 최고 가격(현재 가격보다 높은 값)을 제시하지 못하면 경매에 참여할 수 없음.
 function bid() public payable {
     require(ended == false, "Auction's been over. Cannot bid now.");
     require(msg.sender != beneficiary, "Beneficiary cannot participate in the auction");
     require(now <= auctionEndTime, "Auction already ended");
     require(msg.value >= minValue, "Check the minimum bidding price.");
     require(msg.value > highestBid, "There already is a higher bid.");
     if(highestBid > 0) {
       pendingReturns[highestBidder] += highestBid;
     }
     highestBid = msg.value;
     highestBidder = msg.sender;
     bidders.push(msg.sender);
     emit HighestBidIncereased(msg.sender, msg.value);
 }
 /// @dev 경매 종료까지 남은 시간을 초(in seconds)로 반환
 function getTimeLeft() public view returns (uint) {
     return (auctionEndTime - now);
 }
 /// @dev 특정 주소가 경매에 참여하여 환불받을 이더량
 /// @param _address 경매 참가자의 주소
 /// @return 경매에 참여한 참가자가 환불 받지 못한 이더
 function getPendingReturnsBy(address _address) view public returns (uint){
     return pendingReturns[_address];
 }
 /// @dev 출금 요청, 경매에 참여한 주소가 호출할 수 있음.
 /// 파라메터 필요하지 않음.
 /// @return bool 출금 성공 여부
 function withdraw() public returns (bool) {
     require(pendingReturns[msg.sender] != 0, "Address did not bid yet or already requests refund.");
     uint amount = pendingReturns[msg.sender];
     if(!msg.sender.send(amount)){
         pendingReturns[msg.sender] = amount;
         return false;
     }
     pendingReturns[msg.sender] = 0;
     return true;
 }
 /// @dev 경매 생성자에 의해 경매 금액을 모두 반환하며 경매를 끝냄.
 /// 현재 최고가로 낙찰함.
 function endAuction() public onlyOwner {
   _end();
   _refund(false);
   beneficiary.transfer(highestBid);
   emit AuctionEnded(highestBidder, highestBid);
 }
 /// @dev 경매 생성자에 의해 경매를 취소함.
 /// 현재 최고 경매가 제시자에게도 환불
 function cancelAuction() public onlyOwner{
   _end();
   _refund(true);
 }
 /// @dev 상태 변수 ended를 조작하는 internal 함수
 function _end() internal{
     require(!ended, "The auction is already over.");
     ended = true;
 }
 /// @dev 환불을 위한 internal 함수
 /// @param isCancelled 경매 취소 인지 낙찰인지
 function _refund(bool isCancelled) internal {
     uint returnToHighestBidder;
     if(isCancelled){
       returnToHighestBidder = pendingReturns[highestBidder] + highestBid;
     } else {
       returnToHighestBidder = pendingReturns[highestBidder];
     }
     for(uint i = 0; i < bidders.length; i++){
       address payable bidder = bidders[i];
       if(bidder != highestBidder){
           if(!bidder.send(pendingReturns[bidder])){
               revert();
           }
       } else {
           if(!bidder.send(returnToHighestBidder)){
               revert();
           }
       }
     }
 }
}