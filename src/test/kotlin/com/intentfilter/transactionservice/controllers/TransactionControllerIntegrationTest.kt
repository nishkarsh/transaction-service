package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.controllers.base.BaseControllerTest
import com.intentfilter.transactionservice.models.CurrencyCode.EUR
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.dtos.TransactionDTO
import com.intentfilter.transactionservice.utilities.AccountsProvider
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.ws.rs.client.Entity.entity
import javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import javax.ws.rs.core.Response.Status.CREATED

class TransactionControllerIntegrationTest : BaseControllerTest() {
    private lateinit var accountsProvider: AccountsProvider

    @BeforeEach
    override fun setUp() {
        super.setUp()
        accountsProvider = AccountsProvider(entityManager)
    }

    @Test
    internal fun shouldTransferMoneyFromRemitterToBeneficiary() {
        val remitterAccount = accountsProvider.createAccount(150.0)
        val beneficiaryAccount = accountsProvider.createAccount(190.0)

        val transactionRequest = TransactionDTO(null, remitterAccount.id, beneficiaryAccount.id, Money(EUR, 50.0))
        val response = target("transaction").request()
            .post(entity(transactionRequest, APPLICATION_JSON_TYPE))

        with(entityManager) {
            refresh(remitterAccount)
            refresh(beneficiaryAccount)
        }

        assertThat(response.status, `is`(CREATED.statusCode))
        assertThat(remitterAccount.balance, `is`(100.0))
        assertThat(beneficiaryAccount.balance, `is`(240.0))
    }
}