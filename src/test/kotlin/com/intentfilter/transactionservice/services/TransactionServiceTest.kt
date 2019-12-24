package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class TransactionServiceTest {
    @Mock
    private lateinit var repository: TransactionRepository
    @Mock
    private lateinit var accountService: AccountService
    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Test
    internal fun shouldTransferMoneyFromRemitterToBeneficiaryAccount(
        @Random transaction: Transaction, @Random remitter: Account, @Random beneficiary: Account, @Random transactionId: UUID
    ) {
        val createdTransaction = transaction.copy(id = transactionId)
        `when`(accountService.getAccountById(transaction.remitterAccountId)).thenReturn(remitter)
        `when`(accountService.getAccountById(transaction.beneficiaryAccountId)).thenReturn(beneficiary)
        `when`(repository.create(transaction)).thenReturn(createdTransaction)

        val returnedTransaction = transactionService.create(transaction)

        val inOrder = inOrder(accountService)
        inOrder.verify(accountService).debit(remitter, transaction.amount)
        inOrder.verify(accountService).credit(beneficiary, transaction.amount)
        assertThat(returnedTransaction, `is`(createdTransaction))
    }
}