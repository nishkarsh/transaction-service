package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.repositories.AccountRepository
import java.util.*
import javax.inject.Inject
import javax.naming.OperationNotSupportedException

open class AccountService @Inject constructor(private val repository: AccountRepository) {
    open fun getAccountById(accountId: UUID): Account? {
        return repository.findAccountById(accountId)
    }

    open fun debit(remitter: Account, amount: Money) {
        if (remitter.currencyCode != amount.currencyCode) {
            throw OperationNotSupportedException("Cross-currency transactions are not supported.")
        }

        repository.deductFromBalance(remitter.id, amount.value)
    }

    open fun credit(beneficiary: Account, amount: Money) {
        if (beneficiary.currencyCode != amount.currencyCode) {
            throw OperationNotSupportedException("Cross-currency transactions are not supported.")
        }

        repository.addToBalance(beneficiary.id, amount.value)
    }
}
