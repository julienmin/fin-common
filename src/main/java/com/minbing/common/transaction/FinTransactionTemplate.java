package com.minbing.common.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

/**
 * 默认提供事物模版，如果不需要配置为false
 */
@Component
@ConditionalOnProperty(name = "fin.transaction.enabled", havingValue = "true", matchIfMissing = true)
public class FinTransactionTemplate {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * REQUIRES方式，抛异常事物回滚
     * @param runnable
     */
    public void executeWithTransaction(Runnable runnable) {
        int oldPropagation = transactionTemplate.getPropagationBehavior();
        try {
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
            transactionTemplate.executeWithoutResult(status -> runnable.run());
        } finally {
            transactionTemplate.setPropagationBehavior(oldPropagation);
        }
    }

    /**
     * REQUIRES方式，抛异常事物回滚
     * @param supplier
     */
    public <T> T executeWithTransaction(Supplier<T> supplier) {
        int oldPropagation = transactionTemplate.getPropagationBehavior();
        try {
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
            return transactionTemplate.execute(status -> supplier.get());
        } finally {
            transactionTemplate.setPropagationBehavior(oldPropagation);
        }
    }


    /**
     * REQUIRES_NEW方式，新开事物，抛异常事物回滚
     * @param runnable
     */
    public void executeWithNewTransaction(Runnable runnable) {
        int oldPropagation = transactionTemplate.getPropagationBehavior();
        try {
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
            transactionTemplate.executeWithoutResult(status -> runnable.run());
        } finally {
            transactionTemplate.setPropagationBehavior(oldPropagation);
        }
    }

    /**
     * REQUIRES_NEW方式，新开事物，抛异常事物回滚
     * @param supplier
     */
    public <T> T executeWithNewTransaction(Supplier<T> supplier) {
        int oldPropagation = transactionTemplate.getPropagationBehavior();
        try {
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
            return transactionTemplate.execute(status -> supplier.get());
        } finally {
            transactionTemplate.setPropagationBehavior(oldPropagation);
        }
    }


    /**
     * 非事物方式执行，即NOT_SUPPORTED
     * @param supplier
     */
    public <T> T executeWithoutTransaction(Supplier<T> supplier) {
        int oldPropagation = transactionTemplate.getPropagationBehavior();
        try {
            transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_NOT_SUPPORTED);
            return transactionTemplate.execute(status -> supplier.get());
        } finally {
            transactionTemplate.setPropagationBehavior(oldPropagation);
        }
    }


    /**
     * 加入事物同步器，事物提交后执行
     * @param runnable
     */
    public static void registerAfterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

}
