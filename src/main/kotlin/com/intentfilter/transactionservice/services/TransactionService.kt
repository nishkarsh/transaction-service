package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import javax.inject.Inject

open class TransactionService @Inject constructor(
    private val repository: TransactionRepository, private val accountService: AccountService
) {
    open fun create(transaction: Transaction): Transaction {
        val remitterAccount = accountService.getAccountById(transaction.remitterAccount.id)
        val beneficiaryAccount = accountService.getAccountById(transaction.beneficiaryAccount.id)

        // TODO Start transaction with both Accounts Locked
        accountService.debit(remitterAccount, Money(transaction.currencyCode, transaction.amount))
        accountService.credit(beneficiaryAccount, Money(transaction.currencyCode, transaction.amount))
        val createdTransaction = repository.create(transaction)
        // TODO Commit transaction and release lock

        return createdTransaction
    }
}
