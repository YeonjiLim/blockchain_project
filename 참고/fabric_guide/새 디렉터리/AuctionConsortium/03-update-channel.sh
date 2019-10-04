./configtxgen -profile AuctionChannel -outputAnchorPeersUpdate copyrightOrgAnchor.tx -channelID auctionchannel001 -asOrg copyrightAssociateOrgMSP
./configtxgen -profile AuctionChannel -outputAnchorPeersUpdate digitalAuthorOrgAnchor.tx -channelID auctionchannel001 -asOrg digitalAuthorOrgMSP

mv copyrightOrgAnchor.tx ./channel-artifacts
mv digitalAuthorOrgAnchor.tx ./channel-artifacts

# update anchor peer
docker exec -e "CORE_PEER_LOCALMSPID=copyrightAssociateOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.copyrightOrg peer channel update -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/copyrightOrgAnchor.tx
docker exec -e "CORE_PEER_LOCALMSPID=digitalAuthorOrgMSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.digitalAuthorOrg peer channel update -o orderer001:7050 -c auctionchannel001 -f /var/hyperledger/production/digitalAuthorOrgAnchor.tx
