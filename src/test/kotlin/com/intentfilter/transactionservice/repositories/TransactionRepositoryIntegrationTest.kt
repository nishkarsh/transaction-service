package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.TransactionServiceApplication
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Transaction
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.glassfish.jersey.test.JerseyTest
import org.hamcrest.core.Is.`is`
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import javax.transaction.Transactional

@ExtendWith(RandomBeansExtension::class)
internal class TransactionRepositoryIntegrationTest : JerseyTest(TransactionServiceApplication()) {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var transactionRepository: TransactionRepository

    @BeforeEach
    override fun setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT)
        transactionRepository = TransactionRepository(entityManagerFactory.createEntityManager())
    }

    @Test
    @Transactional
    internal fun shouldSaveTransaction(
        @Random(excludes = ["id", "createdAt", "updatedAt"]) beneficiary: Account,
        @Random(excludes = ["id", "createdAt", "updatedAt"]) remitter: Account,
        @Random(excludes = ["id", "createdAt", "updatedAt"]) transaction: Transaction
    ) {
        var createdTransaction: Transaction? = null

        doInJPA(this::entityManagerFactory) {
            it.persist(beneficiary)
            it.persist(remitter)

            val validTransaction = transaction.copy(remitterAccount = remitter, beneficiaryAccount = beneficiary)
            createdTransaction = TransactionRepository(it).create(validTransaction)
        }

        val fetchedTransaction = transactionRepository.findById(createdTransaction!!.id!!)

        assertNotNull(fetchedTransaction!!.id)
        assertNotNull(fetchedTransaction.createdAt)
        assertThat(fetchedTransaction, `is`(createdTransaction))
    }
}