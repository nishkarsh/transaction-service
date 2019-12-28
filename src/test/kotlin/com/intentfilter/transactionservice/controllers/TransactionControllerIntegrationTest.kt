package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.controllers.base.BaseControllerTest
import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.CurrencyCode.EUR
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.dtos.TransactionDTO
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.hibernate.testing.util.ReflectionUtil.setField
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.junit.Assert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.ws.rs.client.Entity.entity
import javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import javax.ws.rs.core.Response.Status.CREATED

@ExtendWith(RandomBeansExtension::class)
class TransactionControllerIntegrationTest : BaseControllerTest() {
    private lateinit var easyRandom: EasyRandom

    @BeforeEach
    override fun setUp() {
        super.setUp()
        easyRandom = EasyRandom(EasyRandomParameters().apply {
            excludeField(FieldPredicates.named("createdAt").and(FieldPredicates.inClass(Account::class.java)))
            excludeField(FieldPredicates.named("updatedAt").and(FieldPredicates.inClass(Account::class.java)))
        })
    }

    @Test
    internal fun shouldTransferMoneyFromRemitterToBeneficiary() {
        val remitterAccount = createAccount(150.0)
        val beneficiaryAccount = createAccount(190.0)

        val transactionRequest = TransactionDTO(null, remitterAccount.id, beneficiaryAccount.id, Money(EUR, 50.0))
        val response = target("transaction").request()
            .post(entity(transactionRequest, APPLICATION_JSON_TYPE))

        entityManager.refresh(remitterAccount)
        entityManager.refresh(beneficiaryAccount)

        assertThat(response.status, `is`(CREATED.statusCode))
        assertThat(remitterAccount.balance, `is`(100.0))
        assertThat(beneficiaryAccount.balance, `is`(240.0))
    }

    private fun createAccount(balance: Double): Account {
        val baseAccount = easyRandom.nextObject(Account::class.java)
        val newAccount = baseAccount.copy(balance = balance, currencyCode = EUR).also {
            setField(it, "id", null)
        }
        doInJPA({ entityManager.entityManagerFactory }) {
            entityManager.persist(newAccount)
        }

        return newAccount
    }
}