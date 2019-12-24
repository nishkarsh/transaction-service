package com.intentfilter.transactionservice.providers

import com.intentfilter.transactionservice.repositories.TransactionRepository
import org.glassfish.hk2.utilities.binding.AbstractBinder

object RepositoriesProvider : AbstractBinder() {
    override fun configure() {
        bindAsContract(TransactionRepository::class.java)
    }
}