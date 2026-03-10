package com.minbing.common.domain;

import com.minbing.common.lang.CommonErrorCode;
import com.minbing.common.lang.FinException;

import java.util.Optional;

/**
 * 仓库通用接口
 */
public interface IRepository<E extends IEntity, S> {

    /**
     * 存储
     * @param entity
     * @return
     */
    S store(E entity);

    /**
     * 更新
     * @param entity
     */
    void restore(E entity);

    /**
     * 模型加载，加载不到时返回空
     * @param entityId
     * @return
     */
    Optional<E> load(S entityId);

    default E loadExist(S entityId) {
        return load(entityId).orElseThrow(() -> FinException.build("Entity not exist, entityId={}", CommonErrorCode.ENTITY_NOT_EXIST, entityId));
    }

    /**
     * 删除，默认不支持
     * @param entityId
     */
    default void delete(S entityId) {
        throw new UnsupportedOperationException();
    }

}
