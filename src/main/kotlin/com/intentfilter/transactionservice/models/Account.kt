package com.intentfilter.transactionservice.models

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Account(
    @Id
    @GeneratedValue(generator = "uuid2")
    val id: UUID,

    @Column(nullable = false)
    val userId: UUID? = null,

    @Column(nullable = false)
    val currencyCode: CurrencyCode? = null,

    @Column(nullable = false, precision = 4)
    val balance: Double? = null,

    @Column(nullable = false)
    val status: AccountStatus? = null,

    @CreationTimestamp
    val createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    val updatedAt: ZonedDateTime? = null
)
