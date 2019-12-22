package com.intentfilter.transactionservice.providers

import com.intentfilter.transactionservice.services.TransactionService
import org.glassfish.hk2.utilities.binding.AbstractBinder

object ServicesProvider : AbstractBinder() {
    override fun configure() {
        bindAsContract(TransactionService::class.java)
    }
}