package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.base.BaseIntegrationTest
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class TransactionRepositoryIntegrationTest : BaseIntegrationTest() {
    private lateinit var transactionRepository: TransactionRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        transactionRepository = TransactionRepository(entityManager)
    }

    @Test
    internal fun shouldSaveTransaction(
        @Random(excludes = ["id", "createdAt", "updatedAt"]) beneficiary: Account,
        @Random(excludes = ["id", "createdAt", "updatedAt"]) remitter: Account,
        @Random(excludes = ["id", "createdAt", "updatedAt"]) transaction: Transaction
    ) {
        entityManager.persist(beneficiary)
        entityManager.persist(remitter)

        val validTransaction = transaction.copy(remitterAccount = remitter, beneficiaryAccount = beneficiary)
        val createdTransaction = transactionRepository.create(validTransaction)
        entityManager.flush()

        val fetchedTransaction = transactionRepository.findById(createdTransaction.id!!)

        assertNotNull(fetchedTransaction!!.id)
        assertNotNull(fetchedTransaction.createdAt)
        assertThat(fetchedTransaction, `is`(createdTransaction))
    }
}