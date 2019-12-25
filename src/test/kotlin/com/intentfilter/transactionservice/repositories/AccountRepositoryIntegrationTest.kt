package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.TransactionServiceApplication
import com.intentfilter.transactionservice.TransactionServiceApplication.Companion.PERSISTENT_UNIT
import com.intentfilter.transactionservice.models.Account
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.glassfish.jersey.test.JerseyTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.hibernate.testing.transaction.TransactionUtil.doInJPA
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

@ExtendWith(RandomBeansExtension::class)
internal class AccountRepositoryIntegrationTest : JerseyTest(TransactionServiceApplication()) {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    override fun setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENT_UNIT)
        accountRepository = AccountRepository(entityManagerFactory.createEntityManager())
    }

    @Test
    internal fun shouldFindAccountById(@Random(excludes = ["id", "createdAt", "updatedAt"]) account: Account) {
        doInJPA(this::entityManagerFactory) {
            it.persist(account)
        }

        val fetchedAccount = accountRepository.findAccountById(account.id)

        assertNotNull(fetchedAccount?.id)
        assertNotNull(fetchedAccount?.createdAt)
        assertNotNull(fetchedAccount?.updatedAt)
    }

    @Test
    internal fun shouldSetAccountBalance(@Random(excludes = ["id"]) account: Account) {
        doInJPA(this::entityManagerFactory) {
            it.persist(account)
            AccountRepository(it).setAccountBalance(account.id, 79.59)
        }

        val updatedAccount = accountRepository.findAccountById(account.id)

        assertThat(updatedAccount?.balance, `is`(79.59))
    }
}