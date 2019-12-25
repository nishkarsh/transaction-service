package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Transaction
import java.util.*
import javax.inject.Inject
import javax.persistence.EntityManager

open class TransactionRepository @Inject constructor(private val entityManager: EntityManager) {
    open fun create(transaction: Transaction): Transaction {
        return transaction.also { entityManager.persist(it) }
    }

    open fun findById(transactionId: UUID): Transaction? {
        return entityManager.find(Transaction::class.java, transactionId)
    }
}
