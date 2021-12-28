
package com.gaoshan.chain.constract.engine;

import com.gaoshan.chain.config.ChainConfig;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.AddressGasProvider;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.storm3j.crypto.Credentials;
import org.storm3j.crypto.ECKeyPair;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.http.HttpService;
import org.storm3j.tx.gas.ContractGasProvider;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
@Component
public abstract class BaseEngine {

    private static final Logger logger = LoggerFactory.getLogger(BaseEngine.class);
    private ChainConfig chainConfig;
    private IssuerConfig issuerConfig;
    private AddressGasProvider addressGasProvider;
//    private RedisService redisService;

//    public BaseEngine(ChainConfig chainConfig, IssuerConfig issuerConfig,AddressGasProvider addressGasProvider) {
//    }

    private <T> T loadContract(
            String contractAddress,
            Object credentials,
            Class<T> cls) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Object contract;
        Method method = cls.getMethod(
                "load",
                String.class,
                Storm3j.class,
                credentials.getClass(),
                ContractGasProvider.class
        );
        Storm3j storm3j = Storm3j.build(new HttpService(chainConfig.getNodeRpc()));
        contract = method.invoke(
                null,
                contractAddress,
                storm3j,
                credentials,
                addressGasProvider);

        return (T) contract;
    }

    /**
     * Reload contract.
     *
     * @param contractAddress the contract address
     * @param privateKey      the privateKey of the sender
     * @param cls             the class
     * @param <T>             t
     * @return the contract
     */
    protected <T> T reloadContract(
            String contractAddress,
            String privateKey,
            Class<T> cls) {
        T contract = null;
        try {
            // load contract
            Credentials credentials = Credentials.create(privateKey);
            contract = loadContract(contractAddress, credentials, cls);
            logger.info(cls.getSimpleName() + " init succ");
        } catch (Exception e) {
            logger.error("load contract :{} failed. Error message is :{}",
                    cls.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (contract == null) {
            throw new RuntimeException();
        }
        return contract;
    }

    /**
     * Gets the contract service.
     *
     * @param contractAddress the contract address
     * @param cls             the class
     * @param <T>             t
     * @return the contract service
     */
    protected <T> T getContractService(String contractAddress, Class<T> cls) {

        T contract = null;
        try {
            // load contract
            Credentials credentials = Credentials.create(issuerConfig.getPrivateKey());
            contract = loadContract(contractAddress, credentials, cls);
            logger.info(cls.getSimpleName() + " init succ");

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("load contract :{} failed. Error message is :{}",
                    cls.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("load contract Exception:{} failed. Error message is :{}",
                    cls.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (contract == null) {
            throw new RuntimeException();
        }
        return contract;
    }

    /**
     * Gets the contract service.
     *
     * @param contractAddress the contract address
     * @param cls             the class
     * @param <T>             t
     * @return the contract service
     */
    protected <T> T getContractService(String contractAddress, Credentials credentials, Class<T> cls) {

        T contract = null;
        try {
            // load contract
            contract = loadContract(contractAddress, credentials, cls);
            logger.info(cls.getSimpleName() + " init succ");

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("load contract :{} failed. Error message is :{}",
                    cls.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("load contract Exception:{} failed. Error message is :{}",
                    cls.getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (contract == null) {
            throw new RuntimeException();
        }
        return contract;
    }

    protected ChainConfig getConfig() {
        return chainConfig;
    }
}
