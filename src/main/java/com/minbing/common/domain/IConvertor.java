package com.minbing.common.domain;

/**
 * 模型与DO互转
 * @param <DO>
 * @param <MODEL>
 */
public interface IConvertor<DO extends BaseDO, MODEL extends IEntity> {

    /**
     * 模型转DO
     * @param model
     * @return
     */
    DO domainToDO(MODEL model);

    /**
     * DO转模型
     * @param doObj
     * @return
     */
    MODEL doToDomain(DO doObj);

}
