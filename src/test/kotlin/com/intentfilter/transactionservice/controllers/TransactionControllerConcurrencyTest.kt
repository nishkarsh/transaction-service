package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.controllers.base.BaseControllerTest
import com.intentfilter.transactionservice.models.CurrencyCode
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.models.dtos.TransactionDTO
import com.intentfilter.transactionservice.utilities.AccountsProvider.AccountPair
import com.intentfilter.transactionservice.utilities.AccountsProvider.AccountPairProvider
import com.intentfilter.transactionservice.utilities.AccountsProvider.AccountPairProvider.Companion.users
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import javax.ws.rs.core.Response

@Execution(ExecutionMode.CONCURRENT)
class TransactionControllerConcurrencyTest : BaseControllerTest() {
    @ParameterizedTest
    @ArgumentsSource(AccountPairProvider::class)
    internal fun shouldHandleMultipleTransactions(accountPair: AccountPair) {
        val remitterAccount = accountPair.remitter
        val beneficiaryAccount = accountPair.beneficiary

        val balanceBefore = remitterAccount.balance!!.plus(beneficiaryAccount.balance!!)

        val transactionRequest =
            TransactionDTO(null, remitterAccount.id, beneficiaryAccount.id, Money(CurrencyCode.EUR, 10.0))
        val response = target("transaction").request()
            .post(Entity.entity(transactionRequest, APPLICATION_JSON_TYPE))

        with(entityManager) {
            refresh(merge(remitterAccount))
            refresh(merge(beneficiaryAccount))
        }

        val transferId = UUID.fromString(response.location.path.split("/")[2])
        val moneyTransfer = entityManager.find(Transaction::class.java, transferId)

        val balanceAfter = remitterAccount.balance!!.plus(beneficiaryAccount.balance!!)

        Assert.assertThat(response.status, `is`(Response.Status.CREATED.statusCode))
        Assertions.assertEquals(balanceBefore, balanceAfter)
        Assert.assertThat(moneyTransfer.amount, `is`(10.0))

        Assert.assertThat(getTotalAccountBalance(), `is`(2628.0))
    }

    private fun getTotalAccountBalance(): Double {
        return entityManager.createQuery("SELECT balance FROM Account account WHERE account.userId IN (:users)")
            .setParameter("users", users.asList())
            .resultList.reduce { totalBalance, accountBalance -> totalBalance as Double + accountBalance as Double } as Double
    }
}
