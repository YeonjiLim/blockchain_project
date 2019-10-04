1. geth --networkid 15 --datadir ~/dev/pri_eth0 --port 30303 --rpc --rpcport 8545 --rpcaddr 0.0.0.0 --rpccorsdomain "*" --rpcapi "admin,net,miner,eth,prc,web3,typool,debug,db,personal" -unlock 0,1 --allow-insecure-unlock console
2. geth --networkid 15 --maxpeers 5 --datadir ~/dev/pri_eth1  --port 30304 console
3. geth --networkid 15 --maxpeers 5 --datadir ~/dev/pri_eth2 --port 30305 console

모든 Account의 비밀번호는 "pass0"

