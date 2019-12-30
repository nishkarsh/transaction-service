package com.intentfilter.transactionservice.providers

import com.intentfilter.transactionservice.services.AccountService
import com.intentfilter.transactionservice.services.TransactionService
import org.glassfish.hk2.utilities.binding.AbstractBinder

class ServicesProvider : AbstractBinder() {
    override fun configure() {
        bindAsContract(TransactionService::class.java)
        bindAsContract(AccountService::class.java)
    }
}