package com.intentfilter.transactionservice

import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.BASE_URI
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PORT
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.logger
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.core.UriBuilder

class TransactionServiceApplication : ResourceConfig() {
    companion object {
        const val PORT = 9000
        const val BASE_URI = "http://localhost/"
        const val RESOURCE_PACKAGES = "com.intentfilter"

        val logger: Logger = LoggerFactory.getLogger(TransactionServiceApplication::class.java)
    }

    init {
        packages(RESOURCE_PACKAGES)
    }
}

fun main(args: Array<String>) {
    JdkHttpServerFactory.createHttpServer(
        UriBuilder.fromUri(BASE_URI).port(PORT).build(), TransactionServiceApplication()
    )

    logger.info("Started listening for requests at $BASE_URI on port $PORT")
}