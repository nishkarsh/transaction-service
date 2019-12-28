package com.intentfilter.transactionservice.services

import com.arjuna.ats.jta.TransactionManager.transactionManager
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import javassist.NotFoundException
import javax.inject.Inject

open class TransactionService @Inject constructor(
    private val repository: TransactionRepository, private val accountService: AccountService
) {
    open fun create(transaction: Transaction): Transaction {
        val transactionManager = transactionManager().also { it.begin(); }

        val remitterAccount = accountService.getAccountById(transaction.remitterAccount.id)
            ?: throw NotFoundException("Account with ID ${transaction.remitterAccount.id} does not exist")

        val beneficiaryAccount = accountService.getAccountById(transaction.beneficiaryAccount.id)
            ?: throw NotFoundException("Account with ID ${transaction.beneficiaryAccount.id} does not exist")

        accountService.debit(remitterAccount, Money(transaction.currencyCode, transaction.amount))
        accountService.credit(beneficiaryAccount, Money(transaction.currencyCode, transaction.amount))
        val createdTransaction = repository.create(transaction)

        transactionManager.transaction.commit()

        return createdTransaction
    }
}
