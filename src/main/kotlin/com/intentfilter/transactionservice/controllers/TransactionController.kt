package com.intentfilter.transactionservice.controllers

import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.services.TransactionService
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/transaction")
class TransactionController @Inject constructor(private val service: TransactionService) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun create(transaction: Transaction): Response {
        val createdTransaction = service.create(transaction)
        return Response.created(URI.create("/transaction/${createdTransaction.id}")).build()
    }
}