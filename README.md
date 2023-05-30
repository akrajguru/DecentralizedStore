Decentralized Storage system using CHORD as the base Distrbuted Hashing Table

Developed a decentralized storage system which can handle storing files as large as multiple GBs and as small as a few bytes on user machines.

Technologies and concepts utilized:
1. Java
2. gRPC
3. Maven
4. Object Oriented Programming concepts
5. Multithreading
6. Implementation based on CHORD - A scalable peer to peer lookup service for Internet Applications: https://pdos.csail.mit.edu/papers/chord:sigcomm01/chord_sigcomm.pdf 
7. SHA-256 hashing protocol
8. JUnit
9. Solidity smart contract
10. Ethereum
11. Web3j

This application allows multiple users participating on a chord ring to act as a distributed file system. This file system can store client data using consistent hashing
and can be retrieved easily using a content id or the file name.

This system overcomes the issue of single point of failure and can adapt and readjust itself after addition of new servers at anytime.

Directions to run the application:

Step 1: The first node can create its own network by starting the application by specifying the port number and information required to enable the crypto layer:

java -cp DecentralizedStore-1.0-SNAPSHOT-jar-with-dependencies.jar store.start.StartNode "port" "contract address" "crypto credentials" "blockchain network IP"

Step 2: All the other joining nodes should know the ip address and the port number for any arbitrary node present in the ring. At the moment there should be one node which was started above. Using that ip address + port our own port we can join the existing ring using the below command:

java -cp DecentralizedStore-1.0-SNAPSHOT-jar-with-dependencies.jar store.start.StartNode "port" "arbitrary host ip address:port" "contract address" "crypto credentials" "blockchain network IP"

Step 3: Follow the switch case menu to store or retrieve files as required

Project Report: http://www.cs.sjsu.edu/faculty/pollett/masters/Semesters/Fall22/ajinkya/CS298_report.pdf
