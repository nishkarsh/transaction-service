package com.intentfilter.transactionservice.models.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.intentfilter.transactionservice.models.Money
import java.util.*

data class TransactionDTO(
    @JsonProperty("id") val id: UUID?,
    @JsonProperty("remitterAccountId") val remitterAccountId: UUID,
    @JsonProperty("beneficiaryAccountId") val beneficiaryAccountId: UUID,
    @JsonProperty("amount") val amount: Money
)