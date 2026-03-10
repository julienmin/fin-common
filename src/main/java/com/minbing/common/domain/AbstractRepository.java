package com.minbing.common.domain;

public abstract class AbstractRepository<DO extends BaseDO, MODEL extends IEntity> {

    /**
     * 获取转换器
     * @return
     */
    protected abstract IConvertor<DO, MODEL> getConvertor();

    /**
     * 模型转换成DO
     * @param model
     * @return
     */
    protected DO domain2DO(MODEL model) {
        return getConvertor().domainToDO(model);
    }

    /**
     * DO转换成模型
     * @param dataObj
     * @return
     */
    protected MODEL do2Domain(DO dataObj) {
        return getConvertor().doToDomain(dataObj);
    }

}
