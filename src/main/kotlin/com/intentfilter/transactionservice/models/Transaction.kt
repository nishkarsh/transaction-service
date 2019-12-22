package com.intentfilter.transactionservice.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Transaction(
    @JsonProperty("id") val id: UUID?,
    @JsonProperty("remitterAccountId") val remitterAccountId: UUID,
    @JsonProperty("beneficiaryAccountId") val beneficiaryAccountId: UUID,
    @JsonProperty("amount") val amount: Money
)