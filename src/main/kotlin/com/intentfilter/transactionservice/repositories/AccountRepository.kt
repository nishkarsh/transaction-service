package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Account
import java.util.*

open class AccountRepository {
    open fun findAccountById(id: UUID): Account? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open fun deductFromBalance(accountId: UUID, amount: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open fun addToBalance(accountId: UUID, amount: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
