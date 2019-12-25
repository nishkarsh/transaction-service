package com.intentfilter.transactionservice.converters

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.models.dtos.TransactionDTO

object TransactionConverter {
    fun from(transactionDto: TransactionDTO): Transaction {
        transactionDto.apply {
            return Transaction(
                id, Account(remitterAccountId), Account(beneficiaryAccountId), amount.value, amount.currencyCode
            )
        }
    }

    fun from(transaction: Transaction): TransactionDTO {
        transaction.apply {
            return TransactionDTO(id, remitterAccount.id, beneficiaryAccount.id, Money(currencyCode, amount))
        }
    }
}