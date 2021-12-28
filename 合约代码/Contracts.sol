pragma solidity ^0.6.0;

import "./Ownable.sol";
import "./ECRecover.sol";
import "./ISealsRouter.sol";
import "./ISeals.sol";
import "./SafeMath.sol";

pragma solidity ^0.6.0;

contract Contracts is Ownable {
    using SafeMath for uint256;
    address sealsRouterAddr;
    // Seals public seals; //签章合约
    struct Signature {
        bytes _signHash; //待签名数据hash
        address _sealaddr; //签章合约地址
        uint8 _sealType; //签章类型
        address _signAddr; //签名地址
        uint256 _signTime; //上链时间
    }
    mapping(bytes => Signature[]) public contractMap; //合同列表(合同原文hash ==> 签名对象)
    mapping(bytes => mapping(bytes => uint256)) public signatureMap; //签署列表索引映射(分别为 合同原文hash ==> (签名对象 ==> 索引))

    event AddSign(bytes contractHash, bytes signHash, address sealaddr, uint8 sealType, address signAddr);
    event SetAddr(address indexed sealsRouterAddr);
    event RevokeEvent(address indexed account, bool indexed status);

    //设置sealsRouterAddr地址
    function setAsealsRouterAddr(address _addr) public onlyOwner {
        require(_addr != address(0), "_addr is 0");
        sealsRouterAddr = _addr;
        emit SetAddr(_addr);
    }

    //合同签章上链
    function addSign(
        bytes memory _contractHash, //合同原文hash
        bytes memory _signHash, //待签名数据hash
        address _sealaddr, //签章合约地址 如果签字，此值传0
        uint8 _sealType, //签章类型
        address _signAddr, //签名地址
        uint8 v, //签名值 vrs
        bytes32 r,
        bytes32 s
    ) public returns (bool) {
        require(!isExist(_contractHash, _signHash, _sealaddr, _sealType, _signAddr), "_sign is exist");
        if (_sealType == 0) {
            require(_sealaddr == address(0), "_sealaddr error");
        } else {
            bool res1 = ISealsRouter(sealsRouterAddr).querySealStatus(_sealaddr);
            require(res1, "seal is revoked");
            bool res2 = ISeals(_sealaddr).query(_sealType, _signAddr);
            require(res2, "_signAddr has not permission");
        }
        address recoverAddr = recover(v, r, s, _signHash);
        require(recoverAddr == _signAddr, "_sign is error");
        Signature[] storage signArry = contractMap[_contractHash];
        signArry.push(Signature(_signHash, _sealaddr, _sealType, _signAddr, block.timestamp));
        signatureMap[_contractHash][abi.encodePacked(_signHash, _sealaddr, _sealType, _signAddr)] = signArry.length;
        emit AddSign(_contractHash, _signHash, _sealaddr, _sealType, _signAddr);
        return true;
    }

    function isExist(
        bytes memory _contractHash,
        bytes memory _signHash, //待签名数据hash
        address _sealaddr, //签章合约地址 如果签字，此值传0
        uint8 _sealType, //签章类型
        address _signAddr //签名地址
    ) public view returns (bool) {
        if (query(_contractHash, _signHash, _sealaddr, _sealType, _signAddr) > 0) {
            return true;
        } else {
            return false;
        }
    }

    //返回索引
    function query(
        bytes memory _contractHash,
        bytes memory _signHash, //待签名数据hash
        address _sealaddr, //签章合约地址 如果签字，此值传0
        uint8 _sealType, //签章类型
        address _signAddr //签名地址
    ) public view returns (uint256) {
        return signatureMap[_contractHash][abi.encodePacked(_signHash, _sealaddr, _sealType, _signAddr)];
    }

    function queryDetail(bytes memory _contractHash, uint256 _index)
        public
        view
        returns (
            bytes memory _signHash,
            address _sealaddr,
            uint8 _sealType,
            address _signAddr,
            uint256 _signTime
        )
    {
        require(contractMap[_contractHash].length >= _index, "_index greater then length");
        Signature memory sign = contractMap[_contractHash][_index];
        return (sign._signHash, sign._sealaddr, sign._sealType, sign._signAddr, sign._signTime);
    }

    function querySignTimel(
        bytes memory _contractHash,
        bytes memory _signHash, //待签名数据hash
        address _sealaddr, //签章合约地址 如果签字，此值传0
        uint8 _sealType, //签章类型
        address _signAddr //签名地址
    ) public view returns (uint256 _signTime) {
        require(isExist(_contractHash, _signHash, _sealaddr, _sealType, _signAddr), "_sign is not exist");
        uint256 index = query(_contractHash, _signHash, _sealaddr, _sealType, _signAddr);
        Signature memory sign = contractMap[_contractHash][index - 1];
        return sign._signTime;
    }

    function recover(
        uint8 v,
        bytes32 r,
        bytes32 s,
        bytes memory typeHashAndData
    ) internal pure returns (address) {
        // bytes memory prefix = "\x19Ethereum Signed Message:\n32";
        bytes32 digest = keccak256(abi.encodePacked("\x19Ethereum Signed Message:\n32", keccak256(typeHashAndData)));
        return ECRecover.recover(digest, v, r, s);
    }
}