package com.intentfilter.transactionservice.providers

import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.process.internal.RequestScoped
import org.slf4j.LoggerFactory
import javax.persistence.EntityManager
import javax.persistence.Persistence

class EntityManagerProvider : AbstractBinder() {
    override fun configure() {
        bindFactory(EntityManagerFactory::class.java).to(EntityManager::class.java).`in`(RequestScoped::class.java)
    }
}

class EntityManagerFactory : Factory<EntityManager> {
    private val logger = LoggerFactory.getLogger(EntityManagerProvider::class.java)

    override fun provide(): EntityManager {
        logger.debug("Providing an instance of EntityManager")
        return Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager()
    }

    override fun dispose(instance: EntityManager?) {
        logger.debug("Disposing an instance of EntityManager")
        instance?.close()
    }
}