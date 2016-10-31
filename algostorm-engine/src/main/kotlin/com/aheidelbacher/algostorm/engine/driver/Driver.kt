package com.aheidelbacher.algostorm.engine.driver

/**
 * A core component of the engine which offers specialized, lower level
 * services.
 */
interface Driver {
    /**
     * Releases all resources acquired by this driver.
     *
     * This method should be idempotent.
     *
     * Invoking any other service after this driver was released may lead to
     * undefined behavior.
     */
    fun release(): Unit
}
