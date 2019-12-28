package com.intentfilter.transactionservice.repositories.base

import com.intentfilter.transactionservice.TransactionServiceApplication
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import org.glassfish.jersey.test.JerseyTest
import org.hibernate.testing.jta.TestingJtaPlatformImpl.transactionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import javax.persistence.EntityManager
import javax.persistence.Persistence

open class BaseIntegrationTest : JerseyTest(TransactionServiceApplication()) {
    private val transactionManager = transactionManager()
    protected lateinit var entityManager: EntityManager

    @BeforeEach
    override fun setUp() {
        transactionManager.begin()
        entityManager = Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager()
    }

    @AfterEach
    override fun tearDown() {
        transactionManager.commit()
        entityManager.close()
    }
}