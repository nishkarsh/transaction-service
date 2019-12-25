package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.CurrencyCode.EUR
import com.intentfilter.transactionservice.models.CurrencyCode.INR
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.repositories.AccountRepository
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import javax.naming.OperationNotSupportedException

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class AccountServiceTest {
    @Mock
    private lateinit var repository: AccountRepository
    @InjectMocks
    private lateinit var service: AccountService

    @Test
    internal fun shouldFindAccountById(@Random accountId: UUID, @Random account: Account) {
        `when`(repository.findAccountById(accountId)).thenReturn(account)

        val fetchedAccount = service.getAccountById(accountId)

        assertThat(fetchedAccount, `is`(account))
    }

    @Test
    internal fun shouldDebitMoneyFromRemitterAccount(@Random account: Account, @Random money: Money) {
        service.debit(account, money.copy(currencyCode = account.currencyCode!!))

        verify(repository).deductFromBalance(account.id, money.value)
    }

    @Test
    internal fun shouldCreditMoneyToBeneficiaryAccount(@Random account: Account, @Random money: Money) {
        service.credit(account, money.copy(currencyCode = account.currencyCode!!))

        verify(repository).addToBalance(account.id, money.value)
    }

    @Test
    internal fun shouldNotDebitMoneyIfCurrencyNotSame(@Random account: Account, @Random money: Money) {
        assertThrows<OperationNotSupportedException> {
            service.debit(account.copy(currencyCode = EUR), money.copy(currencyCode = INR))
        }
    }

    @Test
    internal fun shouldNotCreditMoneyIfCurrencyNotSame(@Random account: Account, @Random money: Money) {
        assertThrows<OperationNotSupportedException> {
            service.credit(account.copy(currencyCode = EUR), money.copy(currencyCode = INR))
        }
    }
}