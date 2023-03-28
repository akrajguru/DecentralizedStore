Decentralized Storage system using CHORD as the base Distrbuted Hashing Table

Developed a storage system application which can handle storing files as large as multiple GBs and as small as a few bytes on user machines.

Technologies and concepts utilized:
1. Java
2. gRPC
3. Maven
4. Object Oriented Programming concepts
5. Multithreading
6. Implementation based on CHORD - A scalable peer to peer lookup service for Internet Applications: https://pdos.csail.mit.edu/papers/chord:sigcomm01/chord_sigcomm.pdf 
7. SHA-256 hashing protocol

This application allows multiple users participating on a chord ring to act as a file system. This file system can store client data using consistent hashing
and can be retrieved easily using a content id or the file name.

This system overcomes the issue of single point of failure and can adapt and readjust itself after addition of new servers at anytime.
