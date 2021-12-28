pragma solidity ^0.6.0;

interface ISealsRouter {
    function createSeal(address _legalAddr, bytes calldata _creditCode) external returns (address sealAddr);

    function revokeSeal(address sealAddr) external returns (bool);

    function querySealStatus(address sealAddr) external view returns (bool);

    //授权吊销参数构建（前端可参考）
    function encodePacked(
        address sealAddr, //签章合约地址
        uint8 _sealType, //签章类型
        address _addr, //授权地址
        uint256 _signTime //授权时间
    ) external pure returns (bytes memory res);

    //查询签章授权情况
    function querySealApprovl(
        address sealAddr, //签章合约地址
        uint8 _sealType, //签章类型
        address _addr //查询地址
    ) external view returns (bool);

    function querySealOwner(address sealAddr) external view returns (address);

    //带签名的企业签章合约授权 任何人都可以调用 但需要携带管理员签名
    function approvalDelegate(
        address sealAddr, //签章合约地址
        uint8 _sealType, //企业签章合约id 1合同章2财务章3合同章 100以后为企业自定义章
        address _addr, //授权对象
        uint256 _signTime, //签名时间
        uint8 v, //ecdsa签名值 v r s
        bytes32 r,
        bytes32 s
    ) external returns (bool);

    //带签名的企业签章合约吊销 任何人都可以调用 但需要携带管理员签名
    function revokeDelegate(
        address sealAddr, //签章合约地址
        uint8 _sealType, //企业签章合约id 1合同章2财务章3合同章 100以后为企业自定义章
        address _addr, //授权对象
        uint256 _signTime, //签名时间
        uint8 v, //ecdsa签名值 v r s
        bytes32 r,
        bytes32 s
    ) external returns (bool);

    function calculateSeal(address _legalAddr, bytes calldata _creditCode) external view returns (address seal);
}