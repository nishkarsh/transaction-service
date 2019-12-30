package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Account
import java.util.*
import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.LockModeType.PESSIMISTIC_WRITE
import javax.persistence.criteria.Path

open class AccountRepository @Inject constructor(private val entityManager: EntityManager) {
    open fun findAccountById(id: UUID): Account? {
        return entityManager.find(Account::class.java, id)
    }

    open fun setAccountBalance(accountId: UUID, amount: Double) {
        val criteriaBuilder = entityManager.criteriaBuilder

        with(criteriaBuilder.createCriteriaUpdate(Account::class.java)) {
            val accountIdField: Path<UUID> = from(Account::class.java)[Account.ID]
            where(criteriaBuilder.equal(accountIdField, accountId)).set(Account.BALANCE, amount)
            entityManager.createQuery(this).executeUpdate()
        }
    }

    open fun acquireLock(vararg accountIds: UUID) {
        entityManager.createQuery("Select account From Account account WHERE account.id IN :accountIds")
            .setParameter("accountIds", accountIds.asList())
            .setLockMode(PESSIMISTIC_WRITE)
            .resultList
    }
}
