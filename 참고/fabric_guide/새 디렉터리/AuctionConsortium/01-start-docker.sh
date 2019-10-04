
# configtxgen 파일생성
# configtxgen --> 채널 구성이나, 업데이트되는(피어가 채널에 참여라던지, 연결정보 변경한다던지) 시에 필요한 정보 파일(~.tx 이나 ~.block 등) 생성
./configtxgen -profile AuctionOrdererGenesis -outputBlock orderer-genesis.block -channelID ordererchannel001
./configtxgen -profile AuctionChannel -outputCreateChannelTx auctionchannel001.tx -channelID auctionchannel001
# 생성된 파일을 이동시킴(auctionchannel001.tx만 이동하며, orderer-genesis.block은 이동시키지 않음)
# 이후에 노드파일이 사용함
mv auctionchannel001.tx ./channel-artifacts


# 4개의 도커 컴포즈 파일을 순서대로 구동시킨다(zookeeper-compose.yaml -> kafka-compose.yaml -> orderer-compose.yaml -> peer-compose.yaml)
# zookeeper 실행
sudo docker-compose -f zookeeper-compose.yaml up -d

# check zookeeper000
nc -z 0.0.0.0 2181
if [ "$?" != 0 ]; then
    echo 'zookeeper000 run fail. Please check docker log.'
else
    echo 'zookeeper000 is active.'
fi

# check zookeeper001
nc -z 0.0.0.0 2182
if [ "$?" != 0 ]; then
    echo 'zookeeper001 run fail. Please check docker log.'
else
    echo 'zookeeper001 is active.'
fi

# check zookeeper002
nc -z 0.0.0.0 2183
if [ "$?" != 0 ]; then
    echo 'zookeeper002 run fail. Please check docker log.'
else
    echo 'zookeeper002 is active.'
fi

sleep 5

# kafka 실행 (kafka는 "docker logs kafka000" 명령어로 로그 확인 필요)
# kafka 로그 상에서 [KafkaServer id=1] started 라는 로그 출력 시 정상 동작(id 값은 kafka별로 상이)
sudo docker-compose -f kafka-compose.yaml up -d

sleep 5

# orderer 실행
sudo docker-compose -f orderer-compose.yaml up -d

# check orderer001
nc -z 0.0.0.0 8050
if [ "$?" != 0 ]; then
    echo 'orderer001 run fail. Please check docker log.'
else
    echo 'orderer001 is active.'
fi

# check orderer002
nc -z 0.0.0.0 7060
if [ "$?" != 0 ]; then
    echo 'orderer002 run fail. Please check docker log.'
else
    echo 'orderer002 is active.'
fi

# check orderer003
nc -z 0.0.0.0 7070
if [ "$?" != 0 ]; then
    echo 'orderer003 run fail. Please check docker log.'
else
    echo 'orderer003 is active.'
fi

sleep 15

# peer 실행
sudo docker-compose -f peer-compose.yaml up -d

# check peer0.copyrightOrg
nc -z 0.0.0.0 7051
if [ "$?" != 0 ]; then
    echo 'peer0.copyrightOrg run fail. Please check docker log.'
else
    echo 'peer0.copyrightOrg is active.'
fi

# check peer0.digitalAuthorOrg
nc -z 0.0.0.0 8051
if [ "$?" != 0 ]; then
    echo 'peer0.digitalAuthorOrg run fail. Please check docker log.'
else
    echo 'peer0.digitalAuthorOrg is active.'
fi