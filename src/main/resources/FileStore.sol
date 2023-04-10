// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.8.2 <0.9.0;

contract FileStore{

    struct Server {
        string name;
        uint256 numFiles;
        string[] fileNames;
    }

    mapping(string => Server) public servers;
    string[] serverNames;

    function addServer(string memory _name, uint256 _numFiles, string[] memory _fileNames) public {
        Server memory newServer = Server({
        name: _name,
        numFiles: _numFiles,
        fileNames: _fileNames
        });
        servers[_name] = newServer;
        serverNames.push(_name);
    }

    function getServerCount() public view returns (uint256) {
        return serverNames.length;
    }

    function getServer(string memory _name) public view returns (string memory, uint256, string[] memory) {
        Server memory server = servers[_name];
        require(bytes(server.name).length > 0, "Server not found");
        return (server.name, server.numFiles, server.fileNames);
    }

    function addFileToServer(string memory _serverKey, string memory _fileName) public {
        Server storage server = servers[_serverKey];
        require(bytes(server.name).length > 0, "Server not found");
        server.fileNames.push(_fileName);
        server.numFiles = server.fileNames.length;
    }

}