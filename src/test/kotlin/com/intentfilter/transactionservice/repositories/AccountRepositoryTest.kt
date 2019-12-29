package com.intentfilter.transactionservice.repositories

import com.intentfilter.transactionservice.models.Account
import com.intentfilter.transactionservice.repositories.base.BaseRepositoryTest
import com.intentfilter.transactionservice.utilities.AccountsProvider
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class AccountRepositoryTest : BaseRepositoryTest() {
    private lateinit var accountsProvider: AccountsProvider
    private lateinit var accountRepository: AccountRepository

    @BeforeEach
    override fun setUp() {
        super.setUp()
        accountRepository = AccountRepository(entityManager)
    }

    @Test
    internal fun shouldFindAccountById(@Random(excludes = ["id", "createdAt", "updatedAt"]) account: Account) {
        entityManager.persist(account)
        entityManager.flush()

        val fetchedAccount = accountRepository.findAccountById(account.id)

        assertNotNull(fetchedAccount?.id)
        assertNotNull(fetchedAccount?.createdAt)
        assertNotNull(fetchedAccount?.updatedAt)
    }

    @Test
    internal fun shouldSetAccountBalance(@Random(excludes = ["id"]) account: Account) {
        entityManager.persist(account)

        accountRepository.setAccountBalance(account.id, 79.59)

        entityManager.refresh(account)
        with(accountRepository.findAccountById(account.id)!!) {
            assertThat(balance, `is`(79.59))
        }
    }
}