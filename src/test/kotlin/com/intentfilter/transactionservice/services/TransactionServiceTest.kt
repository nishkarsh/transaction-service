package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import com.nhaarman.mockitokotlin2.*
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import javassist.NotFoundException
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import javax.transaction.TransactionManager
import javax.ws.rs.NotAllowedException

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class TransactionServiceTest {
    @Mock
    private lateinit var repository: TransactionRepository
    @Mock
    private lateinit var accountService: AccountService
    @Mock
    private lateinit var transactionManager: TransactionManager

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Test
    internal fun shouldTransferMoneyFromRemitterToBeneficiaryAccount(
        @Random transaction: Transaction, @Random remitter: Account, @Random beneficiary: Account, @Random transactionId: UUID
    ) {
        val createdTransaction = transaction.copy(id = transactionId)
        whenever(accountService.getAccountById(transaction.remitterAccount.id)).thenReturn(remitter)
        whenever(accountService.getAccountById(transaction.beneficiaryAccount.id)).thenReturn(beneficiary)
        whenever(repository.create(transaction)).thenReturn(createdTransaction)

        val returnedTransaction = transactionService.create(transaction)

        val inOrder = inOrder(accountService)
        inOrder.verify(accountService).debit(remitter, Money(transaction.currencyCode, transaction.amount))
        inOrder.verify(accountService).credit(beneficiary, Money(transaction.currencyCode, transaction.amount))
        assertThat(returnedTransaction, `is`(createdTransaction))
    }

    @Test
    internal fun shouldAcquireLockOnAccountsBeforeTransfer(
        @Random transaction: Transaction, @Random remitter: Account, @Random beneficiary: Account, @Random transactionId: UUID
    ) {
        whenever(accountService.getAccountById(transaction.remitterAccount.id)).thenReturn(remitter)
        whenever(accountService.getAccountById(transaction.beneficiaryAccount.id)).thenReturn(beneficiary)

        transactionService.create(transaction)

        val inOrder = inOrder(accountService)
        inOrder.verify(accountService).acquireLock(transaction.remitterAccount.id, transaction.beneficiaryAccount.id)
        inOrder.verify(accountService).debit(remitter, Money(transaction.currencyCode, transaction.amount))
        inOrder.verify(accountService).credit(beneficiary, Money(transaction.currencyCode, transaction.amount))
    }

    @Test
    internal fun shouldThrowErrorWhenRemitterAccountNotFound(@Random transaction: Transaction) {
        whenever(accountService.getAccountById(transaction.remitterAccount.id)).thenReturn(null)

        assertThrows<NotFoundException> { transactionService.create(transaction) }

        verify(accountService, never()).debit(any(), any())
        verify(accountService, never()).credit(any(), any())
    }

    @Test
    internal fun shouldThrowErrorWhenBeneficiaryAccountNotFound(@Random transaction: Transaction, @Random remitter: Account) {
        whenever(accountService.getAccountById(transaction.remitterAccount.id)).thenReturn(remitter)
        whenever(accountService.getAccountById(transaction.beneficiaryAccount.id)).thenReturn(null)

        assertThrows<NotFoundException> { transactionService.create(transaction) }

        verify(accountService, never()).debit(any(), any())
        verify(accountService, never()).credit(any(), any())
    }

    @Test
    internal fun shouldThrowErrorIfRemitterSameAsBeneficiary(@Random transaction: Transaction, @Random account: Account) {
        assertThrows<NotAllowedException> {
            transactionService.create(transaction.copy(remitterAccount = account, beneficiaryAccount = account))
        }

        verify(accountService, never()).debit(any(), any())
        verify(accountService, never()).credit(any(), any())
    }
}