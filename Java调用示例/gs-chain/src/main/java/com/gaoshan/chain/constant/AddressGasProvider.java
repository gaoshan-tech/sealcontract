package com.gaoshan.chain.constant;

import com.gaoshan.chain.config.ChainConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.http.HttpService;
import org.storm3j.tx.gas.ContractGasProvider;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
@Component
@AllArgsConstructor
public class AddressGasProvider implements ContractGasProvider {
    public static ConcurrentHashMap<String, BigInteger> GAS_LIMIT = new ConcurrentHashMap<>();
    private final ChainConfig chainConfig ;
    @Override
    public BigInteger getGasPrice(String s) {
        return getGasPrice();
    }

    @SneakyThrows
    @Override
    public BigInteger getGasPrice() {
        return Storm3j.build(new HttpService(chainConfig.getNodeRpc())).fstGasPrice().send().getGasPrice();
    }

    @Override
    public BigInteger getGasLimit(String address) {
        BigInteger gasLimit = GAS_LIMIT.get(address);
        if (gasLimit != null) {
            return gasLimit;
        }
        return chainConfig.getGasLimit();
    }
    @Override
    public BigInteger getGasLimit() {
        return chainConfig.getGasLimit();
    }
}
