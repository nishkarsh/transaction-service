package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Transaction

open class TransactionRepository {
    open fun create(transaction: Transaction): Transaction {
        TODO("Save transaction")
    }
}
