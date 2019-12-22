package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Transaction

open class TransactionService {
    open fun create(transaction: Transaction): Transaction {
        TODO("Validate accounts, balances and create transaction")
    }
}
