# 代码说明：
```text
│── gs-chain		// 合约调用模块																			

│  │── pom.xml

│  └── src

│    │── main

│    │  │── java

│    │  │  └── com

│    │  │    └── gaoshan

│    │  │      └── chain

│    │  │        │── GsChainApplication.java				//启动类

│    │  │        │── config												//配置项

│    │  │        │── constant											//一些常量

│    │  │        │── constract

│    │  │        │  │── ContractsContract.java			//合约合约的java实现

│    │  │        │  │── IdentityContract.java				//身份合约的java实现

│    │  │        │  │── SealsRouterContract.java		//公司签章合约的java实现

│    │  │        │  └── engine

│    │  │        │    │── BaseEngine.java						//引擎基类

│    │  │        │    │── ContractsContractEngine.java	/合约合约引擎

│    │  │        │    │── IdentityContractEngine.java		//身份合约合约引擎

│    │  │        │    └── SealsRouterContractEngine.java		//公司签章合约引擎

│    │  │        │── entity											//一些实体类

│    │  │        │── service

│    │  │        │  │── ContractServiceImpl.java	//合同合约的Service层，封装了签名添加、查询相关方法

│    │  │        │  │── SealsServiceImpl.java		//签章合约的Service层，封装了签章生成、授权、查询相关方法

│    │  │        │  └── VcServiceImpl.java 			//身份合约的Service层，封装了vc相关方法

│    │  │        │── util

│    │  │        │  │── CredentialPojoUtils.java	//vc相关类

│    │  │        │  │── CredentialUtils.java		  //vc相关类

│    │  │        │  │── DataToolUtils.java			//数据构造转化相关类

│    │  │        │  │── DateUtils.java					//日期生成转化相关类

│    │  │        │  └── GIdUtils.java						//did生成转化相关类

│    │  │        └── validator									//校验相关类

│    │  └── resources

│    │    └── application.yml									//项目配置

│    └── test.java.com.gaoshan.chain. service.

│                							└── ChainTest.java	//测试类，所有方法的调用示例均在此可以找到

│── gs-common 														// 基础模块

└── pom.xml															 // maven配置
```


