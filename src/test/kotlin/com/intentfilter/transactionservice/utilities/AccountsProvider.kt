package com.intentfilter.transactionservice.utilities

import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.models.CurrencyCode.EUR
import org.hibernate.testing.jta.TestingJtaPlatformImpl.transactionManager
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.hibernate.testing.util.ReflectionUtil.setField
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.UUID.fromString
import java.util.stream.Stream
import javax.persistence.EntityManager
import javax.persistence.Persistence.createEntityManagerFactory

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

    // TODO Refactor this
    class AccountPairProvider : ArgumentsProvider {
        private var accountsPool: Array<Account>
        private val entityManager: EntityManager = createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager()
        private val easyRandom = EasyRandom(EasyRandomParameters().apply {
            excludeField(FieldPredicates.named("createdAt").and(FieldPredicates.inClass(Account::class.java)))
            excludeField(FieldPredicates.named("updatedAt").and(FieldPredicates.inClass(Account::class.java)))
        })

        init {
            transactionManager().begin()
            accountsPool = arrayOf(
                easyRandom.nextObject(Account::class.java).copy(currencyCode = EUR, balance = 130.0, userId = users[0])
                    .also { setField(it, "id", null) }.also { entityManager.persist(it) },
                easyRandom.nextObject(Account::class.java).copy(currencyCode = EUR, balance = 500.0, userId = users[1])
                    .also { setField(it, "id", null) }.also { entityManager.persist(it) },
                easyRandom.nextObject(Account::class.java).copy(currencyCode = EUR, balance = 1002.0, userId = users[2])
                    .also { setField(it, "id", null) }.also { entityManager.persist(it) },
                easyRandom.nextObject(Account::class.java).copy(currencyCode = EUR, balance = 976.0, userId = users[3])
                    .also { setField(it, "id", null) }.also { entityManager.persist(it) },
                easyRandom.nextObject(Account::class.java).copy(currencyCode = EUR, balance = 20.0, userId = users[4])
                    .also { setField(it, "id", null) }.also { entityManager.persist(it) }
            )
            transactionManager().commit()
        }

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                AccountPair(accountsPool[1], accountsPool[0]),
                AccountPair(accountsPool[0], accountsPool[3]),
                AccountPair(accountsPool[3], accountsPool[1]),
                AccountPair(accountsPool[4], accountsPool[3]),
                AccountPair(accountsPool[4], accountsPool[0]),
                AccountPair(accountsPool[1], accountsPool[4]),
                AccountPair(accountsPool[1], accountsPool[2]),
                AccountPair(accountsPool[3], accountsPool[4]),
                AccountPair(accountsPool[4], accountsPool[0]),
                AccountPair(accountsPool[2], accountsPool[4])
            ).map { Arguments.of(it) }
        }

        companion object {
            val users = arrayOf(
                fromString("d25a5c64-715f-4081-b072-95ecd27f15ba"),
                fromString("0f88d688-c919-47a3-b2c0-ef85101832af"),
                fromString("48acab0e-314d-43ff-b9f6-61a7aa367620"),
                fromString("0b4499fd-d529-435b-bd09-f78b2ad28111"),
                fromString("93955be6-b7b4-4d89-b533-e72c6bb27357")
            )
        }
    }

    class AccountPair(val remitter: Account, val beneficiary: Account)
}
