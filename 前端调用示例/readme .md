





# 基于墨客联盟链合同签名校验文档

##  链基础信息

墨客联盟链浏览器：http://39.99.191.4:4200/

墨客联盟链钱包和相关文档：http://app.moacchain.net/qWallet/qWallet/#/walletPage

墨客联盟链RPC接口：http://web3.consortium.moacchain.net/

## 创建钱包

### 调用方法 

```yaml
//创建钱包
function createWallet () {
  return storm3.fst.accounts.create()
}
```

<!--示例代码-->

`let wallet = createWallet()`

<!--输出结果-->

 `{`
  `address: '0x096a45dA893A50ADBE8d31D1c5ca2F317BB30A14',`
  `privateKey: '0x1d3f3be4500e39423e46fe3c26fd6d271b6b379a2752c1def66a0b5fc334e2cb',`
  `signTransaction: [Function: signTransaction],`
  `sign: [Function: sign],`
  `encrypt: [Function: encrypt]`
`}`

## 生成keystore

### 调用方法

```yaml
//生成keystore
function createKeyStore (privateKey, password) {
  return storm3.fst.accounts.encrypt(privateKey, password)
}
```

<!--示例代码-->

<!--此处设置keystore密码为：123456-->

`let keystore = createKeyStore(wallet.privateKey, '123456')`

<!--输出结果-->

 `{`
  `version: 3,`
  `id: '51774f5f-b58c-482f-9307-df751983fe8b',`
  `address: '096a45da893a50adbe8d31d1c5ca2f317bb30a14',`
  `crypto: {`
    `ciphertext: '0e4728bd2636774076541116327df851207368a90e53b7ac6ab0bc6875b5c1f9',`
    `cipherparams: { iv: '8d20fbe1c457d89219fa338cd8497f6e' },`
    `cipher: 'aes-128-ctr',`
    `kdf: 'scrypt',`
    `kdfparams: {`
      `dklen: 32,`
      `salt: '2bfdcd85cbad9e3a7437980012d85f00ce66e15b84851e92529c4ccabdd09aa9',`
      `n: 8192,`
      `r: 8,`
      `p: 1`
    `},`
    `mac: '7ed611f1c0117d0522ab31d9ba9c29fe51c74a3bc554358f402b7a88d9f9592a'`
  `}`
`}`

## 用keystore恢复钱包

### 调用方法

```yaml
//创建钱包
function createWallet () {
  return storm3.fst.accounts.create()
}
```

<!--示例代码-->

<!--使用生成keystore时设置的密码：123456-->

`let decWallet = createWalletByKeyStore(keystore, '123456')`

<!--输出结果-->

 `{`
  `address: '0x096a45dA893A50ADBE8d31D1c5ca2F317BB30A14',`
  `privateKey: '0x1d3f3be4500e39423e46fe3c26fd6d271b6b379a2752c1def66a0b5fc334e2cb',`
  `signTransaction: [Function: signTransaction],`
  `sign: [Function: sign],`
  `encrypt: [Function: encrypt]`
`}`

## 用私钥恢复钱包

### 调用方法

```yaml
//用私钥创建钱包
function createWalletByPrivateKey (privateKey) {
  return storm3.fst.accounts.privateKeyToAccount(privateKey)
}
```

<!--示例代码-->

`let account = createWalletByPrivateKey('0x1d3f3be4500e39423e46fe3c26fd6d271b6b379a2752c1def66a0b5fc334e2cb')`

<!--输出结果-->

 `{`
  `address: '0x096a45dA893A50ADBE8d31D1c5ca2F317BB30A14',`
  `privateKey: '0x1d3f3be4500e39423e46fe3c26fd6d271b6b379a2752c1def66a0b5fc334e2cb',`
  `signTransaction: [Function: signTransaction],`
  `sign: [Function: sign],`
  `encrypt: [Function: encrypt]`
`}`

## 合同签名流程

### 待签名数据 

以下数据为测试数据 仅供参考

```yaml
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
```

### 签名方法

```yaml
//调用签名方法
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
```

<!--示例代码（示例的签名私钥：0x0ed948bbcc18d79b3b6551f13675e14ea776d8af6e199d8c194f77d2749cffea）-->

`let signatureFirst = getSignature(account, signatureJsonFirst)`

<!--得到签名值-->

`HNnnVS/209YaqNFqXDNr6j3qM/sSsCE441icdgJJDStSfsMy63ypyRhbtlr0vkYZ/Vhgfr4wYq0w7/j1bOO9O3Y=`

<!--将得到的签名值写入原有待签名json中 提交服务器上链-->

```yaml
//第一次完整签名数据
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
  proof: {
    created: "1640141534767",
    creator: "did:gid:95dbf7398ab60fd2c0f18ada51e78258e904ac3d",
    signature: "HNnnVS/209YaqNFqXDNr6j3qM/sSsCE441icdgJJDStSfsMy63ypyRhbtlr0vkYZ/Vhgfr4wYq0w7/j1bOO9O3Y=",
    type: "Secp256k1"
  },
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
```

### 第二次待签名数据

##### 将第一次的完整签名包裹在第二次签名的nestSignature字段中，得到第二次待签名的数据

```yaml
let signatureJsonSecond = {
  context: "https://gaoshan.co/gsidentity/credentials/v1",
  contractHash: "+BFJkiq/rhmDa8Wrv9S0kU4Rc3Z15szPlxOvRL0uM2A=",
  id: "5032be6c-da75-4b44-a1d0-0f07cfede87e",
  nestSignature: signatureJsonFirst, //第一次签名的完整数据，此处方便文档展示，填写了变量
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
```

<!--调用签名方法，得到签名后将签名数据加入到待签名json中，提交到服务器上链（步骤同第一次签名，此处略）-->

## 校验签名

### 调用方法

```yaml
//验证签名方法
function recoverSignature (signItem, isVc) { // isVc = true 验证VC 
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
  let vrsStr = storm3.utils.bytesToHex(Array.from(Base64.toUint8Array(signature))).replace('0x', '')
  let v = '0x' + vrsStr.substring(0, 2)
  let r = '0x' + vrsStr.substring(2, 64 + 2)
  let s = '0x' + vrsStr.substring(64 + 2, vrsStr.length)
  let recoverDid = isVc ? web3.eth.accounts.recover(storm3.utils.sha3(storm3.utils.hexToBytes(messageHash)), v, r, s, true) : web3.eth.accounts.recover(messageHash, v, r, s)
  console.log(recoverDid)
  if (recoverDid.replace('0x', '').toUpperCase() === creator.replace('did:gid:', '').toUpperCase()) {
    return true
  } else {
    return false
  }
}
```

<!--示例代码-->

<!--校验VC签名是否正确-->

`let res = recoverSignature(signatureJsonSecond, true)`

<!--校验合同签名是否正确-->

`let res = recoverSignature(signatureJsonSecond, false)`

<!--输出结果-->

<!--true or false-->

## 链上校验

### 调用初始化合约方法

```yaml
//初始化合约 
function initContract (abi, address) {
  return new storm3.fst.Contract(abi, address)
}
```

<!--示例代码-->

`if (!identityContract) {`

  `identityContract = initContract(identityAbi, identityContractAddr)`

`}`

#### 查询智能合约校验签名的VC在签名的时候是否被吊销/校验合同签名是否上链成功

```yaml
//链上校验方法
function checkMoacChain (signItem, isVC) {//signItem签名数据，isVC：true 校验VC false 校验合同
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
```

<!--示例代码-->

<!--校验签名时VC是否有效-->

`let res= checkMoacChain(signatureJsonFirst, true)`

`if (res) {`

  `console.log('链上校验签名时VC有效')`

`} else {`

  `console.log('链上校验签名时VC无效')`

`}`

<!--校验合同签名是否有效-->

`let resFileFirst = checkMoacChain(signatureJsonFirst, false)`

`if (resFileFirst) {`

  `console.log('链上校验合同上链成功')`

`} else {`

  `console.log('链上校验合同上链失败')`

`}`

## 其他方法

### 随机生成uuid

```yaml
//生成uuid随机方法
function uuid () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = Math.random() * 16 | 0
    var v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}
```

### 签名JSON排序的函数

```yaml
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
```

### 验签用的claim排序算法

```yaml
//验签用的claim排序算法
function vcSha3 (obj) {
  var newkey = Object.keys(obj).sort()
  var value = ''
  for (var i = 0; i < newkey.length; i++) {
    value += newkey[i] + storm3.utils.sha3(obj[newkey[i]])
  }
  return value
}
```

