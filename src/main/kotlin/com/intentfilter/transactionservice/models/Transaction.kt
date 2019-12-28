package com.intentfilter.transactionservice.models

import org.hibernate.annotations.CreationTimestamp
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Transaction(
    @Id
    @GeneratedValue(generator = "uuid2")
    val id: UUID?,

    @ManyToOne @JoinColumn(name = "remitterAccountId")
    val remitterAccount: Account,

    @ManyToOne @JoinColumn(name = "beneficiaryAccountId")
    val beneficiaryAccount: Account,

    @Column(nullable = false)
    val amount: Double,

    @Column(nullable = false)
    val currencyCode: CurrencyCode,

    @CreationTimestamp
    val createdAt: ZonedDateTime? = null
)