package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.TransactionServiceApplication
import org.glassfish.jersey.test.JerseyTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.ws.rs.core.Response

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HealthCheckControllerTest : JerseyTest(TransactionServiceApplication()) {
    @BeforeAll
    fun before() {
        super.setUp()
    }

    @Test
    internal fun shouldReturnOkIfHealthy() {
        val response = target("health-check").request().get()
        assertThat(response.status, `is`(Response.Status.OK.statusCode))
    }

    @AfterAll
    fun after() {
        super.tearDown()
    }
}