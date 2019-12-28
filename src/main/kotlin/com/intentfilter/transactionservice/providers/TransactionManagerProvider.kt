package com.intentfilter.transactionservice.providers

import com.arjuna.ats.jta.TransactionManager.transactionManager
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder
import javax.transaction.TransactionManager

object TransactionManagerProvider : AbstractBinder() {
    override fun configure() {
        bindFactory(TransactionManagerFactory::class.java).to(TransactionManager::class.java)
    }
}

class TransactionManagerFactory : Factory<TransactionManager> {
    override fun provide(): TransactionManager {
        return transactionManager()
    }

    override fun dispose(instance: TransactionManager?) {
        // Nothing to do
    }
}