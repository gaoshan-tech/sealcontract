pragma solidity ^0.6.0;

interface ISeals {
    function approval(uint8 _sealType, address _addr) external returns (bool);

    function approvalDelegate(
        uint8 _sealType,
        address _addr,
        uint256 _signTime,
        uint8 v,
        bytes32 r,
        bytes32 s
    ) external returns (bool);

    function encodePacked(
        uint8 _sealType,
        address _addr,
        uint256 _signTime
    ) external view returns (bytes memory res);

    function revoke(uint8 _sealType, address _addr) external returns (bool);

    function revokeDelegate(
        uint8 _sealType,
        address _addr,
        uint256 _signTime,
        uint8 v,
        bytes32 r,
        bytes32 s
    ) external returns (bool);

    function query(uint8 _sealType, address _addr) external view returns (bool);
}