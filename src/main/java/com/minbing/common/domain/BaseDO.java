package com.minbing.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseDO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    /**
     * 可选
     */
    private Integer version;
}
