
var Base64 = require('js-Base64')
var Storm3 = require('storm3')
var Web3 = require('Web3')
var storm3 = new Storm3(
  new Storm3.providers.HttpProvider('http://web3.consortium.moacchain.net/')
)

var web3 = new Web3()
const contractsContractAddr = '0x2F91F7520aDAC5B6a49eBE3bd4237bf8C42c563f' // 合同合约
const identityContractAddr = '0xA9538359d782757EFdc5a6A65B98E2af5fA4f29f' // 身份合约
const sealsContractAddr = '0x18CDF7a0fB4D0D28Cb623d02ae812b3618691c2d' // 企业签章合约
var identityContract, contractsContract
const identityAbi = [
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': false,
        'internalType': 'bytes',
        'name': 'account',
        'type': 'bytes'
      }
    ],
    'name': 'AddIdentityEvent',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': false,
        'internalType': 'bytes',
        'name': 'account',
        'type': 'bytes'
      }
    ],
    'name': 'LoseEfficacyEvent',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'previousOwner',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'OwnershipTransferred',
    'type': 'event'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_identity',
        'type': 'bytes'
      }
    ],
    'name': 'addIdentity',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'initialize',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      }
    ],
    'name': 'isValid',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      }
    ],
    'name': 'lastEfficacyTime',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'owner',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_identity',
        'type': 'bytes'
      }
    ],
    'name': 'queryEfficacyTime',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_identity',
        'type': 'bytes'
      },
      {
        'internalType': 'uint256',
        'name': '_time',
        'type': 'uint256'
      }
    ],
    'name': 'queryIdentity',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'renounceOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_identity',
        'type': 'bytes'
      }
    ],
    'name': 'revokeIdentity',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'transferOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  }
]
const sealsRouterAbi = [
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'indexed': true,
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      }
    ],
    'name': 'ApprovalDelegate',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'previousOwner',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'OwnershipTransferred',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      }
    ],
    'name': 'Revoke',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'indexed': true,
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      }
    ],
    'name': 'RevokeDelegate',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'legalAddr',
        'type': 'address'
      },
      {
        'indexed': false,
        'internalType': 'bytes',
        'name': 'creditCode',
        'type': 'bytes'
      },
      {
        'indexed': false,
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'indexed': false,
        'internalType': 'uint256',
        'name': 'length',
        'type': 'uint256'
      }
    ],
    'name': 'SealsCreated',
    'type': 'event'
  },
  {
    'inputs': [],
    'name': 'INIT_CODE_SEAl_HASH',
    'outputs': [
      {
        'internalType': 'bytes32',
        'name': '',
        'type': 'bytes32'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'name': 'allSeals',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'allSealsLength',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      },
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      },
      {
        'internalType': 'uint8',
        'name': 'v',
        'type': 'uint8'
      },
      {
        'internalType': 'bytes32',
        'name': 'r',
        'type': 'bytes32'
      },
      {
        'internalType': 'bytes32',
        'name': 's',
        'type': 'bytes32'
      }
    ],
    'name': 'approvalDelegate',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': '_legalAddr',
        'type': 'address'
      },
      {
        'internalType': 'bytes',
        'name': '_creditCode',
        'type': 'bytes'
      }
    ],
    'name': 'calculateSeal',
    'outputs': [
      {
        'internalType': 'address',
        'name': 'seal',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': '_legalAddr',
        'type': 'address'
      },
      {
        'internalType': 'bytes',
        'name': '_creditCode',
        'type': 'bytes'
      }
    ],
    'name': 'createSeal',
    'outputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      },
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      }
    ],
    'name': 'encodePacked',
    'outputs': [
      {
        'internalType': 'bytes',
        'name': 'res',
        'type': 'bytes'
      }
    ],
    'stateMutability': 'pure',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      },
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      }
    ],
    'name': 'getSeal',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'initialize',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'owner',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      }
    ],
    'name': 'querySealApprovl',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      }
    ],
    'name': 'querySealOwner',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      }
    ],
    'name': 'querySealStatus',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'renounceOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_addr',
        'type': 'address'
      },
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      },
      {
        'internalType': 'uint8',
        'name': 'v',
        'type': 'uint8'
      },
      {
        'internalType': 'bytes32',
        'name': 'r',
        'type': 'bytes32'
      },
      {
        'internalType': 'bytes32',
        'name': 's',
        'type': 'bytes32'
      }
    ],
    'name': 'revokeDelegate',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'sealAddr',
        'type': 'address'
      }
    ],
    'name': 'revokeSeal',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'name': 'sealStatus',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'transferOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  }
]
const contractsAbi = [
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'account',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'bool',
        'name': 'status',
        'type': 'bool'
      }
    ],
    'name': 'ContractEvent',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'previousOwner',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'OwnershipTransferred',
    'type': 'event'
  },
  {
    'anonymous': false,
    'inputs': [
      {
        'indexed': true,
        'internalType': 'address',
        'name': 'account',
        'type': 'address'
      },
      {
        'indexed': true,
        'internalType': 'bool',
        'name': 'status',
        'type': 'bool'
      }
    ],
    'name': 'RevokeEvent',
    'type': 'event'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_contractHash',
        'type': 'bytes'
      },
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': 'v',
        'type': 'uint8'
      },
      {
        'internalType': 'bytes32',
        'name': 'r',
        'type': 'bytes32'
      },
      {
        'internalType': 'bytes32',
        'name': 's',
        'type': 'bytes32'
      }
    ],
    'name': 'addSign',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      },
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'name': 'contractMap',
    'outputs': [
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'initialize',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_contractHash',
        'type': 'bytes'
      },
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      }
    ],
    'name': 'isExist',
    'outputs': [
      {
        'internalType': 'bool',
        'name': '',
        'type': 'bool'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'owner',
    'outputs': [
      {
        'internalType': 'address',
        'name': '',
        'type': 'address'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_contractHash',
        'type': 'bytes'
      },
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      }
    ],
    'name': 'query',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_contractHash',
        'type': 'bytes'
      },
      {
        'internalType': 'uint256',
        'name': '_index',
        'type': 'uint256'
      }
    ],
    'name': 'queryDetail',
    'outputs': [
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      },
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '_contractHash',
        'type': 'bytes'
      },
      {
        'internalType': 'bytes',
        'name': '_signHash',
        'type': 'bytes'
      },
      {
        'internalType': 'address',
        'name': '_sealaddr',
        'type': 'address'
      },
      {
        'internalType': 'uint8',
        'name': '_sealType',
        'type': 'uint8'
      },
      {
        'internalType': 'address',
        'name': '_signAddr',
        'type': 'address'
      }
    ],
    'name': 'querySignTimel',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '_signTime',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [],
    'name': 'renounceOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      },
      {
        'internalType': 'bytes',
        'name': '',
        'type': 'bytes'
      }
    ],
    'name': 'signatureMap',
    'outputs': [
      {
        'internalType': 'uint256',
        'name': '',
        'type': 'uint256'
      }
    ],
    'stateMutability': 'view',
    'type': 'function'
  },
  {
    'inputs': [
      {
        'internalType': 'address',
        'name': 'newOwner',
        'type': 'address'
      }
    ],
    'name': 'transferOwnership',
    'outputs': [],
    'stateMutability': 'nonpayable',
    'type': 'function'
  }
]

if (!identityContract) {
  identityContract = initContract(identityAbi, identityContractAddr)
}
if (!contractsContract) {
  contractsContract = initContract(contractsAbi, contractsContractAddr)
}

let wallet = createWallet()
console.log('创建钱包', wallet)
let keystore = createKeyStore(wallet.privateKey, '123456')
console.log('创建keystore,密码123456', keystore)
let decWallet = createWalletByKeyStore(keystore, '123456')
console.log('用keystore恢复钱包,密码123456', decWallet)


//第一次待签名数据
let signatureJsonFirst = {
  context: "https://gaoshan.co/gsidentity/credentials/v1",
  contractHash: "+BFJkiq/rhmDa8Wrv9S0kU4Rc3Z15szPlxOvRL0uM2A=",
  id: "12d11948-59e4-4f63-9162-91c58f4c1636",
  nestSignature: {
    context: null,
    contractHash: null,
    id: null,
    nestSignature: null,
    proof: null,
    sealsClaim: null,
    signDate: null,
    signerVC: null,
    type: null
  },
  proof: null,
  sealsClaim: {
    0: [{
      height: 18,
      position: 0,
      sign: {
        picUrl: "https://testyinji.gaoshan.co/oss/gs2021/20211217/1639715201308-491f094b-8e82-440a-92d5-20357124bf44.png"
      },
      signIndex: 0,
      signType: 0,
      target: {
        name: "张三",
        phone: "15000000001"
      },
      uuid: "12d11948-59e4-4f63-9162-91c58f4c1636",
      width: 60,
      x: 590.279527559055,
      y: 1098.1338582677167
    }]
  },
  signDate: 1640141534706,
  signerVC: {
    claim: {
      did: "did:gid:95dbf7398ab60fd2c0f18ada51e78258e904ac3d",
      name: "张三",
      type: "IDCard"
    },
    context: "https://gaoshan.co/gsidentity/credentials/v1",
    expirationDate: 1955445343808,
    id: "73410afc-c4ec-434b-ae63-be4b5029128e",
    issuanceDate: 1640085343808,
    issuer: "did:gid:743fcae563f978f194767b8da1babbe0f62bbbaa",
    proof: {
      created: "1640085343808",
      creator: "did:gid:743fcae563f978f194767b8da1babbe0f62bbbaa",
      signature: "HITd2EjqKx+13JNAFqrW9DprMwbF1OrhstVY0UsavK/NC/Ilis2tBmydo4SxFtSpMMSjxFYELrRH42/LOD4Nuxs=",
      type: "Secp256k1"
    }
  },
  type: 0
}
let account = createWalletByPrivateKey('0x0ed948bbcc18d79b3b6551f13675e14ea776d8af6e199d8c194f77d2749cffea')
console.log(account)
let signatureFirst = getSignature(account, signatureJsonFirst)
console.log('签名值：', signatureFirst)

signatureJsonFirst.proof = {
  created: "1640141534767",
  creator: "did:gid:95dbf7398ab60fd2c0f18ada51e78258e904ac3d",
  signature: signatureFirst,
  type: "Secp256k1"
}

//第二次待签名数据
let signatureJsonSecond = {
  context: "https://gaoshan.co/gsidentity/credentials/v1",
  contractHash: "+BFJkiq/rhmDa8Wrv9S0kU4Rc3Z15szPlxOvRL0uM2A=",
  id: "5032be6c-da75-4b44-a1d0-0f07cfede87e",
  nestSignature: signatureJsonFirst,
  proof: null,
  sealsClaim: {
    0: [{
      height: 42,
      position: 0,
      sign: {
        picUrl: "https://testyinji.gaoshan.co/oss/gs2021/20211221/f2a01f9eafa1ed526260398799fa0d2e163398edf5c99d1231c2cbe86a9bb987.png"
      },
      signIndex: 0,
      signType: 1,
      target: {
        name: "张三",
        phone: "15000000001"
      },
      uuid: "5032be6c-da75-4b44-a1d0-0f07cfede87e",
      width: 42,
      x: 272.745,
      y: 683.92875
    }]
  },
  signDate: 1640238530934,
  signerVC: {
    claim: {
      code: "1233211234567",
      did: "did:gid:564befcc7d861f0693c21f4e41ef90f460d5d19f",
      legalDid: "did:gid:95dbf7398ab60fd2c0f18ada51e78258e904ac3d",
      legalName: "张三",
      name: "测试1",
      type: "government"
    },
    context: "https://gaoshan.co/gsidentity/credentials/v1",
    expirationDate: 1955445343882,
    id: "4d01c89e-70db-437a-bd4e-8cdf16e27a98",
    issuanceDate: 1640085343882,
    issuer: "did:gid:743fcae563f978f194767b8da1babbe0f62bbbaa",
    proof: {
      created: "1640085343882",
      creator: "did:gid:743fcae563f978f194767b8da1babbe0f62bbbaa",
      signature: "HM0dwwZKLxDFXVr2COORKIheJP+bzEpMB5vM5kG49LHUD7Muq2+v2Rnw17MhPzlDqNddxRpDe0aoMfP7s4gobzk=",
      type: "Secp256k1"
    }
  },
  type: 1
}

let signatureSecond = getSignature(account, signatureJsonSecond)
console.log('签名值：', signatureSecond)
// HHAVqvNbO5D+ad2QXJe7INBLneTGwaW77Eu74HZBurtqLA9C1n5vWDXd5uKES5MLlegOhGbPXrwh1d848HtpYcc=
signatureJsonSecond.proof = {
  created: "1640238531000",
  creator: "did:gid:95dbf7398ab60fd2c0f18ada51e78258e904ac3d",
  signature: signatureSecond,
  type: "Secp256k1"
}
console.log('================分割线===============')
console.log('校验第一次VC签名')
recoverSignature(signatureJsonFirst, true)
console.log('================分割线===============')
console.log('链上校验签名时VC是否有效')
console.log('================分割线===============')
let resVCFirst = checkMoacChain(signatureJsonFirst, true)
if (resVCFirst) {
  console.log('链上校验签名时VC有效')
} else {
  console.log('链上校验签名时VC无效')
}
console.log('================分割线===============')
console.log('校验第一次合同签名')
recoverSignature(signatureJsonFirst, false)
let resFileFirst = checkMoacChain(signatureJsonFirst, false)
if (resFileFirst) {
  console.log('链上校验合同上链成功')
} else {
  console.log('链上校验合同上链失败')
}
console.log('================分割线===============')
console.log('校验第二次VC签名')
recoverSignature(signatureJsonSecond, true)
let resVCSecond = checkMoacChain(signatureJsonSecond, true)
if (resVCSecond) {
  console.log('链上校验签名时VC有效')
} else {
  console.log('链上校验签名时VC无效')
}
console.log('================分割线===============')
console.log('校验第二次合同签名')
recoverSignature(signatureJsonSecond, false)
let resFileSecond = checkMoacChain(signatureJsonSecond, false)
if (resFileSecond) {
  console.log('链上校验合同上链成功')
} else {
  console.log('链上校验合同上链失败')
}
console.log('================分割线===============')
function getSignature (decWallet, signatureJson) {
  let signHash = storm3.utils.sha3(JSON.stringify(signatureJson))
  let signStr = decWallet.sign(storm3.utils.sha3(storm3.utils.hexToBytes(signHash)))
  let vrsArr = []
  vrsArr.push(
    ...storm3.utils.hexToBytes(signStr.v),
    ...storm3.utils.hexToBytes(signStr.r),
    ...storm3.utils.hexToBytes(signStr.s)
  )
  return Base64.fromUint8Array(new Uint8Array(vrsArr))
}

//生成uuid随机方法
function uuid () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = Math.random() * 16 | 0
    var v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

//初始化合约 
function initContract (abi, address) {
  return new storm3.fst.Contract(abi, address)
}

//创建钱包
function createWallet () {
  return storm3.fst.accounts.create()
}
//生成keystore
function createKeyStore (privateKey, password) {
  return storm3.fst.accounts.encrypt(privateKey, password)
}
//用私钥创建钱包
function createWalletByPrivateKey (privateKey) {
  return storm3.fst.accounts.privateKeyToAccount(privateKey)
}
//用keystore创建钱包
function createWalletByKeyStore (keystore, password) {
  return storm3.fst.accounts.decrypt(keystore, password)
}
// 排序的函数
function objKeySort (obj) {
  var newkey = Object.keys(obj).sort() // 先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
  var newObj = {} // 创建一个新的对象，用于存放排好序的键值对
  for (var i = 0; i < newkey.length; i++) { // 遍历newkey数组
    if (Object.prototype.toString.call(obj[newkey[i]]) === '[object Object]') { // 如果value是对象递归sort
      obj[newkey[i]] = objKeySort(obj[newkey[i]])
    } else if (Array.isArray(obj[newkey[i]])) { // 如果value是数组递归sort
      for (var j = 0; j < obj[newkey[i]].length; j++) {
        obj[newkey[i]][j] = objKeySort(obj[newkey[i]][j])
      }
    }
    newObj[newkey[i]] = obj[newkey[i]] // 向新创建的对象中按照排好的顺序依次增加键值对
  }
  return newObj // 返回排好序的新对象
}
//验证签名方法
function recoverSignature (signItem, isVc) { // isVc = true 验证VC 
  // console.log(signItem)
  let signJson = JSON.parse(JSON.stringify(signItem))
  let messageHash = ''
  let signature = isVc ? signJson.signerVC.proof.signature : signJson.proof.signature
  let creator = isVc ? signJson.signerVC.proof.creator : signJson.proof.creator
  if (isVc) {
    signJson.signerVC.proof = null
    let ssha3 = vcSha3(signJson.signerVC.claim)
    signJson.signerVC.claim = ssha3
    messageHash = storm3.utils.sha3(JSON.stringify(objKeySort(signJson.signerVC)))
  } else {
    signJson.proof = null
    messageHash = storm3.utils.sha3(storm3.utils.hexToBytes(storm3.utils.sha3(JSON.stringify(objKeySort(signJson)))))
  }
  console.log('messageHash:', messageHash)
  let vrsStr = storm3.utils.bytesToHex(Array.from(Base64.toUint8Array(signature))).replace('0x', '')
  let v = '0x' + vrsStr.substring(0, 2)
  let r = '0x' + vrsStr.substring(2, 64 + 2)
  let s = '0x' + vrsStr.substring(64 + 2, vrsStr.length)
  console.log('vrs:', v, r, s)
  let recoverDid = isVc ? web3.eth.accounts.recover(storm3.utils.sha3(storm3.utils.hexToBytes(messageHash)), v, r, s, true) : web3.eth.accounts.recover(messageHash, v, r, s)
  console.log(recoverDid)
  if (recoverDid.replace('0x', '').toUpperCase() === creator.replace('did:gid:', '').toUpperCase()) {
    let aa = isVc ? '身份验证成功' : '签名验证成功'
    console.log(aa)
    // return true
  } else {
    let aa = isVc ? '身份验证失败' : '签名验证失败'
    console.log(aa)
    // return false
  }
}
//验签用的claim排序算法
function vcSha3 (obj) {
  var newkey = Object.keys(obj).sort()
  var value = ''
  for (var i = 0; i < newkey.length; i++) {
    value += newkey[i] + storm3.utils.sha3(obj[newkey[i]])
  }
  return value
}


function checkMoacChain (signItem, isVC) {
  return new Promise(function (resolve, reject) {
    let signJson = JSON.parse(JSON.stringify(signItem))
    if (isVC) {
      let vc = signJson.signerVC
      let signDate = signJson.signDate
      let ssha3 = vcSha3(vc.claim)
      vc.claim = ssha3
      let messageHash = storm3.utils.sha3(JSON.stringify(objKeySort(vc)))
      identityContract.methods
        .queryIdentity(storm3.utils.fromUtf8(messageHash), parseInt(signDate / 1000))
        .call()
        .then(function (resp) {
          resolve(resp)
        })
    } else {
      const _contractHash = signJson.contractHash // 合同原⽂hash
      const _sealType = signJson.type // 签章类型
      const _sealAddr = _sealType === 0 ? '0x0000000000000000000000000000000000000000' : signJson.signerVC.claim.did.replace('did:gid:', '0x') // 签章合约地址
      const _signAddr = signJson.proof.creator.replace('did:gid:', '0x') // 签名地址
      signJson.proof = null
      const _signHash = storm3.utils.sha3(JSON.stringify(objKeySort(signJson))) // 待签名数据hash
      contractsContract.methods.isExist(storm3.utils.fromUtf8(_contractHash), _signHash, _sealAddr, _sealType, _signAddr).call().then(function (resp) {
        resolve(resp)
      })
    }
  })
}