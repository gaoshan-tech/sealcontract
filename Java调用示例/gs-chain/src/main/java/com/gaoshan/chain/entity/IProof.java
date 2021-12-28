

package com.gaoshan.chain.entity;


import cn.hutool.core.util.StrUtil;

import java.util.Map;

/**
 * proof接口.
 *
 *
 */
public interface IProof {
    
    /**
     * 从proof中获取key对应value.
     * @param proof proofMap数据
     * @param key 要获取的数据的key
     * @return 返回key的数据
     */
    public default Object getValueFromProof(Map<String, Object> proof, String key) {
        if (proof != null) {
            return proof.get(key);
        }
        return null;
    }
    
    /**
     * 将Object转换成String.
     * @param obj 从proof中取出的object
     * @return 返回key的字符串数据
     */
    public default String toString(Object obj) {
        if (obj != null) {
            return String.valueOf(obj);
        }
        return StrUtil.EMPTY;
    }
}
