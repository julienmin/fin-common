package com.minbing.common.domain;

import java.io.Serializable;

public interface IEntity extends Serializable {

    /**
     * 模型自检
     */
    void validate();

}
