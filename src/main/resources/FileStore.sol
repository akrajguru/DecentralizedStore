// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.8.2 <0.9.0;
//pragma solidity ^0.8.12;
//import "./math/SignedMath.sol";
//pragma solidity ^0.8.17;
//import "@openzeppelin/contracts/utils/Strings.sol";

contract FileStoreContract {
    struct Block {
        string fileHash;
        string randomNumber;
        bytes32 verificationHash;
        string[] ownersPaid;
        uint256 amountToBePaid;
    }
    struct Owner{
        string ownerName;
        int count;
        address[] moneyCollected;
    }

    mapping(string => Owner[]) public ownerList;
    string public ipAddress;
    //string public conCat;

    mapping(string => Block) public blockMap;

    function addBlock(
        string memory fileHash,
        string memory randomNumber,
        bytes32 verificationHash,
        string memory _ownerName
    ) private {
        Block memory newBlock;
        Owner memory newOwner;
        newBlock.fileHash = fileHash;
        newBlock.randomNumber = randomNumber;
        newBlock.verificationHash = verificationHash;
        blockMap[fileHash] = newBlock;
        newOwner.ownerName=_ownerName;
        newOwner.count=3;
        newOwner.moneyCollected = new address[](3);
        ownerList[fileHash].push(newOwner);
    }
    // cost of transanction for one node 160000000000000
    // so value should be 3*200000000000000
    // around 0.37 usd

    function addFileBlock(
        string memory fileHash,
        string memory randomNumber,
        bytes32 verificationHash,
        string memory _ownerName
    ) public payable returns (string memory ip) {
        require(msg.value == 600000000000000 wei);
        require(bytes(ipAddress).length > 0);
        addBlock(fileHash, randomNumber, verificationHash, _ownerName);
        return ipAddress;
    }
    //file1,10568,0x3d5c3927ab5924163a067fb04397589649bae9f09bea03154391d49c4e3d2690,ajow
    //file1,2d0ec7431ec5da4a36c5898f77c06eb7487fb7ee65f22cce3a764e68d54cddaa

    function heartBeat(string memory arbNode) public {
        ipAddress = arbNode;
    }

    function hashVerifier(string memory fileHash, string memory myHash)
    public
    returns (bool verified)
    {
        bytes32 verificationHash = blockMap[fileHash].verificationHash;
        string memory num = blockMap[fileHash].randomNumber;
        string memory conCat = string(abi.encodePacked(myHash, num));
        bytes32 hashed = keccak256(abi.encodePacked(conCat));
        if (hashed == verificationHash) {
            return true;
        }
        return false;
    }

    function getPaid(string memory fileHash, string memory myHash)
    public
    payable
    returns (string[] memory paidOwnerList)
    {
        require(hashVerifier(fileHash, myHash));
        Owner[] memory ownArray =ownerList[fileHash];
        uint payCounter=0;
        //uint[] memory indexes;
        string[] memory send = new string[](ownArray.length);

        for(uint i =0; i<ownerList[fileHash].length;i++){
            //Owner memory own = ownerList[fileHash][i];
            //address[] memory addresses = new address[](3);
            //addresses=own.moneyCollected;
            //address[] memory addresses = own.moneyCollected;
            if(ownerList[fileHash][i].moneyCollected.length==3){
                if (ownerList[fileHash][i].moneyCollected[0]==address(0)) {
                    // if (addresses[0] == msg.sender) {
                    //     string[] memory ret;
                    //     ret[0] = "already paid";
                    //     return ret;
                    // }else{
                    send[payCounter]=ownerList[fileHash][i].ownerName;
                    ++payCounter;
                    ownerList[fileHash][i].moneyCollected[0] = msg.sender;
                    //own.moneyCollected=addresses;
                    //ownerList[fileHash].moneyCollected=addresses;
                    //}

                }else if (ownerList[fileHash][i].moneyCollected[1]==address(0)) {
                    if (ownerList[fileHash][i].moneyCollected[0] == msg.sender) {
                        string[] memory ret;
                        ret[0] = "already paid";
                        return ret;
                    }else{
                        send[payCounter]=ownerList[fileHash][i].ownerName;
                        ++payCounter;
                        ownerList[fileHash][i].moneyCollected[1] = msg.sender;
                        //own.moneyCollected=addresses;
                        //ownerList[fileHash].moneyCollected=addresses;
                    }
                } else if (ownerList[fileHash][i].moneyCollected[2]==address(0)) {
                    if (ownerList[fileHash][i].moneyCollected[0] == msg.sender || ownerList[fileHash][i].moneyCollected[1] == msg.sender) {
                        string[] memory ret;
                        ret[0] = "already paid";
                        return ret;
                    }else{
                        send[payCounter]=ownerList[fileHash][i].ownerName;
                        ++payCounter;
                        ownerList[fileHash][i].moneyCollected[2] = msg.sender;
                        //own.moneyCollected=addresses;
                        //ownerList[fileHash].moneyCollected=addresses;
                    }
                }
                //ownArray[i]=own;
                // }else if(addresses[2]==address(0)){
                //     send[payCounter]=own.ownerName;
                //     ++payCounter;
                //     addresses[0]= msg.sender;
                //     //ownerList[fileHash].moneyCollected=addresses;
                // }
                if(ownerList[fileHash][i].moneyCollected[2]!=address(0)){
                    delete ownerList[fileHash][i];
                }
            }
        }

        if (payCounter > 0) {

            uint256 amount = (200000000000000 * payCounter);
            payable(msg.sender).transfer(amount);
        }

        return send;
    }

    function getRandomNumberForVerification(string memory fileHash)
    public
    view
    returns (string memory rand)
    {
        return blockMap[fileHash].randomNumber;
    }

    function getArr(string memory fileHash,uint index)
    public
    view
    returns (address[] memory arr)
    {
        return ownerList[fileHash][index].moneyCollected;
    }

    function getIpAddress()
    public
    view
    returns (string memory  ipAddr)
    {
        return ipAddress;
    }


}
