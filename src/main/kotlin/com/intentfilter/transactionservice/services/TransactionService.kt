package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.repositories.TransactionRepository
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.transaction.TransactionManager
import javax.ws.rs.NotAllowedException

open class TransactionService @Inject constructor(
    private val repository: TransactionRepository, private val accountService: AccountService,
    private val transactionManager: TransactionManager
) {
    private val logger = LoggerFactory.getLogger(TransactionService::class.java)

    open fun create(transaction: Transaction): Transaction {
        if (isRemitterSameAsBeneficiary(transaction)) {
            throw NotAllowedException("Account transfers from an account to same account is not allowed")
        }

        logger.info("Initiating an account transfer from account ${transaction.remitterAccount.id} " +
                "to account ${transaction.beneficiaryAccount.id} for amount ${transaction.currencyCode} ${transaction.amount}")

        transactionManager.begin()
        try {
            accountService.acquireLock(transaction.remitterAccount.id, transaction.beneficiaryAccount.id)
            logger.info("Lock acquired on accounts ${transaction.remitterAccount.id} and ${transaction.beneficiaryAccount.id}")

            val remitterAccount = accountService.getAccountById(transaction.remitterAccount.id)
                ?: throw NotFoundException("Account with ID ${transaction.remitterAccount.id} does not exist")
            val beneficiaryAccount = accountService.getAccountById(transaction.beneficiaryAccount.id)
                ?: throw NotFoundException("Account with ID ${transaction.beneficiaryAccount.id} does not exist")

            accountService.debit(remitterAccount, Money(transaction.currencyCode, transaction.amount))
            accountService.credit(beneficiaryAccount, Money(transaction.currencyCode, transaction.amount))

            return repository.create(transaction).also {
                transactionManager.commit()
                logger.info("Account transfer for amount ${transaction.amount} is complete. TransactionID: ${transaction.id}")
            }
        } catch (exception: Exception) {
            logger.error("An error occurred during account transfer, rolling back transaction and releasing lock")
            throw exception.also { transactionManager.rollback() }
        }
    }

    private fun isRemitterSameAsBeneficiary(transaction: Transaction) =
        transaction.beneficiaryAccount.id == transaction.remitterAccount.id
}
