package com.intentfilter.transactionservice.providers

import org.glassfish.hk2.api.Factory
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.process.internal.RequestScoped
import javax.persistence.EntityManager
import javax.persistence.Persistence

object EntityManagerProvider : AbstractBinder() {
    override fun configure() {
        bindFactory(EntityManagerFactory::class.java).to(EntityManager::class.java).`in`(RequestScoped::class.java)
    }
}

internal class EntityManagerFactory : Factory<EntityManager> {
    override fun provide(): EntityManager {
        return Persistence.createEntityManagerFactory("transactions").createEntityManager()
    }

    override fun dispose(instance: EntityManager?) {
        instance?.close()
    }
}