

package com.gaoshan.chain.entity;

import com.gaoshan.chain.util.DataToolUtils;

import java.io.Serializable;

public interface JsonSerializer extends Serializable {

    public default String toJson() {
        return DataToolUtils.serialize(this);
    }
}
