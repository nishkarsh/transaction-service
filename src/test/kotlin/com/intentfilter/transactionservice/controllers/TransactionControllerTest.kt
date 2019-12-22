package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.services.TransactionService
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.net.URI
import javax.ws.rs.core.Response

@Extensions(ExtendWith(MockitoExtension::class), ExtendWith(RandomBeansExtension::class))
internal class TransactionControllerTest {
    @Mock
    private lateinit var service: TransactionService
    @InjectMocks
    private lateinit var controller: TransactionController

    @Test
    internal fun shouldCreateTransaction(@Random transaction: Transaction, @Random createdTransaction: Transaction) {
        `when`(service.create(transaction)).thenReturn(createdTransaction)

        val response = controller.create(transaction)

        assertThat(response.status, `is`(Response.Status.CREATED.statusCode))
        assertThat(response.location, `is`(URI.create("/transaction/${createdTransaction.id}")))
    }
}