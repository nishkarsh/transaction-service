package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import javax.inject.Inject

open class TransactionService @Inject constructor(
    private val repository: TransactionRepository, private val accountService: AccountService
) {

    open fun create(transaction: Transaction): Transaction {
        val remitterAccount = accountService.getAccountById(transaction.remitterAccountId)
        val beneficiaryAccount = accountService.getAccountById(transaction.beneficiaryAccountId)

        // TODO Start transaction with both Accounts Locked
        accountService.debit(remitterAccount, transaction.amount)
        accountService.credit(beneficiaryAccount, transaction.amount)
        val createdTransaction = repository.create(transaction)
        // TODO Commit transaction and release lock

        return createdTransaction
    }
}
