# channel create
# 피어들끼리 통신할 채널 생성
# peer channel create -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/fabric/production/auctionchannel001.tx --outputBlock /var/hyperledger/production/auctionchannel001-genesis.block
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel create -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/auctionchannel001.tx --outputBlock /var/hyperledger/production/auctionchannel001-genesis.block

# channel join 
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel fetch config /var/hyperledger/production/auctionchannel001-genesis.block -o orderer001:7050 -c auctionchannel001
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel join -o orderer001:7050 -b /var/hyperledger/production/auctionchannel001-genesis.block
docker exec -e "CORE_PEER_LOCALMSPIP=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel fetch config /var/hyperledger/production/auctionchannel001-genesis.block -o orderer001:7050 -c auctionchannel001
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel join -o orderer001:7050 -b /var/hyperledger/production/auctionchannel001-genesis.block