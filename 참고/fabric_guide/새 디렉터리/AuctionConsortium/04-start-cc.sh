docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer chaincode install -n asset -v 0.1 -l node -p /var/hyperledger/chaincode/node
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode install -n asset -v 0.1 -l node -p /var/hyperledger/chaincode/node

# nodejs chaincode instantiate
# 체인코드 도커 컨테이너를 생성 및 구동
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer chaincode instantiate -o orderer001:7050 -C auctionchannel001 -n asset -v 0.1 -c '{"Args":["init"]}' -P "OR('copyrightAssociateOrgMSP.member','digitalAuthorOrgMSP.member')"