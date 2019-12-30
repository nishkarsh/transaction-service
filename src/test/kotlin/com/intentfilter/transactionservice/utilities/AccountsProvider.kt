package com.intentfilter.transactionservice.utilities

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.CurrencyCode.EUR
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.hibernate.testing.util.ReflectionUtil.setField
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import javax.persistence.EntityManager

class AccountsProvider(private val entityManager: EntityManager) {
    private var easyRandom: EasyRandom = EasyRandom(EasyRandomParameters().apply {
        excludeField(FieldPredicates.named("createdAt").and(FieldPredicates.inClass(Account::class.java)))
        excludeField(FieldPredicates.named("updatedAt").and(FieldPredicates.inClass(Account::class.java)))
    })

    fun createAccount(balance: Double): Account {
        val baseAccount = easyRandom.nextObject(Account::class.java)
        return baseAccount.copy(balance = balance, currencyCode = EUR).also {
            setField(it, "id", null)
        }.also { account -> doInJPA({ entityManager.entityManagerFactory }) { entityManager.persist(account) } }
    }
}