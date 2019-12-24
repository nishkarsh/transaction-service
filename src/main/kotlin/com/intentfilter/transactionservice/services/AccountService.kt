package com.intentfilter.transactionservice.services

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Money
import java.util.*

open class AccountService {
    open fun getAccountById(accountId: UUID): Account {
        TODO("Get account ID from repository")
    }

    open fun debit(remitter: Account, amount: Money) {
        TODO("Debit money from remitter account")
    }

    open fun credit(beneficiary: Account, amount: Money) {
        TODO("Credit money to beneficiary account")
    }
}
