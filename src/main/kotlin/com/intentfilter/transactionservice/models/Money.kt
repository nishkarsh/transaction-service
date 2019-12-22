package com.intentfilter.transactionservice.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Money(
    @JsonProperty("currencyCode") val currencyCode: String,
    @JsonProperty("value") val value: Double
)
