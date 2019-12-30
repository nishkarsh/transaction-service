package com.intentfilter.transactionservice.providers

import org.slf4j.LoggerFactory
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ExceptionHandlerProvider : Throwable(), ExceptionMapper<Throwable> {
    private val logger = LoggerFactory.getLogger(ExceptionHandlerProvider::class.java)

    override fun toResponse(exception: Throwable?): Response {
        logger.error("An exception occurred", exception)
        return Response.status(500).entity(exception?.localizedMessage).type("text/plain").build()
    }
}