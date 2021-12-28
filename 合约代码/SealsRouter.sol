pragma solidity ^0.6.0;


import "./Ownable.sol";
import "./ISealsRouter.sol";
import "./Seals.sol";


contract SealsRouter is Ownable, ISealsRouter {
    mapping(address => mapping(bytes => address)) public getSeal;
    address[] public allSeals;
    bytes32 public INIT_CODE_SEAl_HASH = keccak256(abi.encodePacked(type(Seals).creationCode));
    mapping(address => bool) public sealStatus; //签章状态列表

    event SealsCreated(address indexed legalAddr, bytes creditCode, address sealAddr, uint256 length);
    event ApprovalDelegate(address indexed sealAddr, uint8 indexed _sealType, address indexed _addr);
    event RevokeDelegate(address indexed sealAddr, uint8 indexed _sealType, address indexed _addr);
    event Revoke(address indexed sealAddr);

    function allSealsLength() external view returns (uint256) {
        return allSeals.length;
    }

    //创建企业签章合约 仅管理员可以调用
    //_legalAddr 法人的did对应的address
    //_creditCode 企业的唯一识别码
    function createSeal(address _legalAddr, bytes calldata _creditCode)
        external
        override
        onlyOwner
        returns (address sealAddr)
    {
        require(_legalAddr != address(0), "SealsFactory: ZERO_ADDRESS");
        require(getSeal[_legalAddr][_creditCode] == address(0), "SealsFactory: Seal_EXISTS"); // 判断此合约是否已创建
        bytes memory bytecode = type(Seals).creationCode;
        bytes32 salt = keccak256(abi.encodePacked(_legalAddr, _creditCode));
        assembly {
            sealAddr := create2(0, add(bytecode, 32), mload(bytecode), salt) //底层调用 创建企业签章合约
        }
        Ownable(sealAddr).initialize(_legalAddr); //初始化企业签章合约的管理员为企业法人
        getSeal[_legalAddr][_creditCode] = sealAddr; //记录签章合约地址对应关系
        allSeals.push(sealAddr); //记录签章合约地址
        sealStatus[sealAddr] = true; //添加签章状态
        emit SealsCreated(_legalAddr, _creditCode, sealAddr, allSeals.length); //产生企业签章合约事件
    }

    function revokeSeal(address sealAddr) public override onlyOwner returns (bool) {
        sealStatus[sealAddr] = false;
        emit Revoke(sealAddr);
        return true;
    }

    function querySealStatus(address sealAddr) public view override returns (bool) {
        return sealStatus[sealAddr];
    }

    //授权吊销参数构建（前端可参考）
    function encodePacked(
        address sealAddr, //签章合约地址
        uint8 _sealType, //签章类型
        address _addr, //授权地址
        uint256 _signTime //授权时间
    ) public pure override returns (bytes memory res) {
        res = abi.encodePacked(sealAddr, _sealType, _addr, _signTime);
    }

    //查询签章授权情况
    function querySealApprovl(
        address sealAddr, //签章合约地址
        uint8 _sealType, //签章类型
        address _addr //查询地址
    ) public view override returns (bool) {
        return ISeals(sealAddr).query(_sealType, _addr);
    }

    function querySealOwner(address sealAddr) public view override returns (address) {
        return Ownable(sealAddr).owner();
    }

    //带签名的企业签章合约授权 任何人都可以调用 但需要携带管理员签名
    function approvalDelegate(
        address sealAddr, //签章合约地址
        uint8 _sealType, //企业签章合约id 1合同章2财务章3合同章 100以后为企业自定义章
        address _addr, //授权对象
        uint256 _signTime, //签名时间
        uint8 v, //ecdsa签名值 v r s
        bytes32 r,
        bytes32 s
    ) public override returns (bool) {
        emit ApprovalDelegate(sealAddr, _sealType, _addr);
        return ISeals(sealAddr).approvalDelegate(_sealType, _addr, _signTime, v, r, s); //工厂路由合约调用具体企业合约的授权方法
    }

    //带签名的企业签章合约吊销 任何人都可以调用 但需要携带管理员签名
    function revokeDelegate(
        address sealAddr, //签章合约地址
        uint8 _sealType, //企业签章合约id 1合同章2财务章3合同章 100以后为企业自定义章
        address _addr, //授权对象
        uint256 _signTime, //签名时间
        uint8 v, //ecdsa签名值 v r s
        bytes32 r,
        bytes32 s
    ) public override returns (bool) {
        emit RevokeDelegate(sealAddr, _sealType, _addr);
        return ISeals(sealAddr).revokeDelegate(_sealType, _addr, _signTime, v, r, s);
    }

    function calculateSeal(address _legalAddr, bytes calldata _creditCode)
        external
        view
        override
        returns (address seal)
    {
        seal = address(
            uint256(
                keccak256(
                    abi.encodePacked(
                        hex"ff",
                        address(this),
                        keccak256(abi.encodePacked(_legalAddr, _creditCode)),
                        // hex'a87c7746e257b041f5678e9de2595484e94e2fefd59a9277410d3e94f5cb0d99' // init code hash
                        keccak256(abi.encodePacked(type(Seals).creationCode))
                    )
                )
            )
        );
    }
}