package com.intentfilter.transactionservice.controllers.base

import com.intentfilter.transactionservice.TransactionServiceApplication
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import org.glassfish.jersey.test.JerseyTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import javax.persistence.EntityManager
import javax.persistence.Persistence

open class BaseControllerTest : JerseyTest(TransactionServiceApplication()) {
    lateinit var entityManager: EntityManager

    @BeforeEach
    override fun setUp() {
        super.setUp()
        entityManager = Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager()
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
        entityManager.close()
    }
}