pragma solidity ^0.6.0;

import "./Ownable.sol";

contract Identity is Ownable {
    mapping(bytes => bool) public isValid; //是否有效

    mapping(bytes => uint256) public lastEfficacyTime; //失效时间

    // uint256 public minimumTokenBalanceForDividends;

    event AddIdentityEvent(bytes account);
    event LoseEfficacyEvent(bytes account);

    //添加身份hash到合约
    //_identity 为身份数据的hash值
    function addIdentity(bytes memory _identity) public onlyOwner returns (bool) {
        require(!isValid[_identity], "identity has exist");//判断是否已添加过了
        isValid[_identity] = true;//身份存入
        emit AddIdentityEvent(_identity);//产生事件
        return true;
    }

    //吊销身份hash到合约 
    //_identity 为身份数据的hash值
    // onlyOwner仅平台管理员可操作
    function revokeIdentity(bytes memory _identity) public onlyOwner returns (bool) {
        require(isValid[_identity], "identity has revoked");//判断是否已吊销过了
        isValid[_identity] = false;//身份吊销
        lastEfficacyTime[_identity] = block.timestamp;//存入吊销时间
        emit LoseEfficacyEvent(_identity);//产生吊销事件
        return true;
    }

    //根据身份hash 和时间查询此身份是否有效
    //_identity 为身份数据的hash值
    //_time 时间
    function queryIdentity(bytes memory _identity, uint256 _time) public view returns (bool) {
        if (isValid[_identity]) {//如果当前身份有效，直接返回true
            return true;
        } else {
            if (lastEfficacyTime[_identity] > _time) {//如果当前身份失效，查询在需要验证的时间前，身份是否有效
                return true;
            }
        }
        return false;
    }

    //根据身份hash 查询此身份吊销时间
    //_identity 为身份数据的hash值
    function queryEfficacyTime(bytes memory _identity) public view returns (uint256) {
        return lastEfficacyTime[_identity];
    }
}