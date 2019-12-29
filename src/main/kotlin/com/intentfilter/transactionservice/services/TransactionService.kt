package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import javassist.NotFoundException
import javax.inject.Inject
import javax.transaction.TransactionManager
import javax.ws.rs.NotAllowedException

open class TransactionService @Inject constructor(
    private val repository: TransactionRepository, private val accountService: AccountService,
    private val transactionManager: TransactionManager
) {
    open fun create(transaction: Transaction): Transaction {
        if (isRemitterSameAsBeneficiary(transaction)) {
            throw NotAllowedException("Account transfers from an account to same account is not allowed")
        }

        transactionManager.begin()

        val remitterAccount = accountService.getAccountById(transaction.remitterAccount.id)
            ?: throw NotFoundException("Account with ID ${transaction.remitterAccount.id} does not exist")

        val beneficiaryAccount = accountService.getAccountById(transaction.beneficiaryAccount.id)
            ?: throw NotFoundException("Account with ID ${transaction.beneficiaryAccount.id} does not exist")

        accountService.debit(remitterAccount, Money(transaction.currencyCode, transaction.amount))
        accountService.credit(beneficiaryAccount, Money(transaction.currencyCode, transaction.amount))
        val createdTransaction = repository.create(transaction)

        transactionManager.commit()

        return createdTransaction
    }

    private fun isRemitterSameAsBeneficiary(transaction: Transaction) =
        transaction.beneficiaryAccount.id == transaction.remitterAccount.id
}
