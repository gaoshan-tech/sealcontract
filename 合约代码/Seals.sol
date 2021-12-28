pragma solidity ^0.6.0;

import "./Ownable.sol";
import "./ISeals.sol";
import "./ECRecover.sol";

contract Seals is Ownable, ISeals {
    mapping(uint8 => mapping(address => bool)) public approvalMap; //授权列表
    mapping(uint8 => mapping(address => uint256)) public lastChangeTime; //最后一次授权吊销时间

    event ApprovalEvent(address indexed account, bool indexed status);
    event RevokeEvent(address indexed account, bool indexed status);

    //企业签章合约授权 仅企业管理员可操作
    function approval(uint8 _sealType, address _addr) public override onlyOwner returns (bool) {
        require(!approvalMap[_sealType][_addr], "_addr has exist");
        approvalMap[_sealType][_addr] = true;
        lastChangeTime[_sealType][_addr] = block.timestamp;
        emit ApprovalEvent(_addr, true);
        return true;
    }

    //带签名的企业签章合约授权 任何人都可以调用 但需要携带签名
    function approvalDelegate(
        uint8 _sealType,
        address _addr,
        uint256 _signTime,
        uint8 v,
        bytes32 r,
        bytes32 s
    ) public override returns (bool) {
        require(!approvalMap[_sealType][_addr], "_addr has exist"); //判断是否已授权
        require(lastChangeTime[_sealType][_addr] < _signTime, "_sign is invalid"); //防止签名数据被重放
        address recoverAddr = recover(v, r, s, abi.encodePacked(address(this), _sealType, _addr, _signTime)); //验证签名
        require(recoverAddr == owner(), "_sign is error"); //验证签名人是否是管理员
        approvalMap[_sealType][_addr] = true; //签章授权
        lastChangeTime[_sealType][_addr] = _signTime; //记录最后一次授权时间
        emit ApprovalEvent(_addr, true); //产生事件
        return true;
    }

    function encodePacked(
        uint8 _sealType,
        address _addr,
        uint256 _signTime
    ) public view override returns (bytes memory res) {
        res = abi.encodePacked(address(this), _sealType, _addr, _signTime);
    }

    function revoke(uint8 _sealType, address _addr) public override onlyOwner returns (bool) {
        require(approvalMap[_sealType][_addr], "_addr has revoked");
        approvalMap[_sealType][_addr] = false;
        lastChangeTime[_sealType][_addr] = block.timestamp;
        emit RevokeEvent(_addr, false);
        return true;
    }

    //带签名的企业签章合约吊销 任何人都可以调用 但需要携带管理员签名
    function revokeDelegate(
        uint8 _sealType,
        address _addr,
        uint256 _signTime,
        uint8 v,
        bytes32 r,
        bytes32 s
    ) public override returns (bool) {
        require(approvalMap[_sealType][_addr], "_addr has revoked"); //判断是否已吊销
        require(lastChangeTime[_sealType][_addr] < _signTime, "_sign is invalid"); //防止签名数据被重放
        address recoverAddr = recover(v, r, s, abi.encodePacked(address(this), _sealType, _addr, _signTime)); //验证签名
        require(recoverAddr == owner(), "_sign is error"); //验证签名人是否是管理员
        approvalMap[_sealType][_addr] = false; //签章吊销
        lastChangeTime[_sealType][_addr] = _signTime; //记录最后一次吊销时间
        emit RevokeEvent(_addr, false); //产生事件
        return true;
    }

    function query(uint8 _sealType, address _addr) public view override returns (bool) {
        if (_addr == owner()) {
            return true;
        }
        return approvalMap[_sealType][_addr];
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
