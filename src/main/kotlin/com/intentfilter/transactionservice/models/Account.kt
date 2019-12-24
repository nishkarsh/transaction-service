package com.intentfilter.transactionservice.models

import java.util.*

data class Account(
    val id: UUID,
    val userId: UUID,
    val balance: Double,
    val currencyCode: CurrencyCode,
    val status: AccountStatus
)
