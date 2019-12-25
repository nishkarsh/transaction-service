package com.intentfilter.transactionservice.converters

import com.intentfilter.transactionservice.models.Money
import com.intentfilter.transactionservice.models.Transaction
import com.intentfilter.transactionservice.models.dtos.TransactionDTO
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.hamcrest.core.Is.`is`
import org.hamcrest.junit.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class TransactionConverterTest {
    @Test
    internal fun shouldConvertFromTransactionToDto(@Random transaction: Transaction) {
        val dto = TransactionConverter.from(transaction)

        assertThat(dto.id, `is`(transaction.id))
        assertThat(dto.remitterAccountId, `is`(transaction.remitterAccount.id))
        assertThat(dto.beneficiaryAccountId, `is`(transaction.beneficiaryAccount.id))
        assertThat(dto.amount, `is`(Money(transaction.currencyCode, transaction.amount)))
    }

    @Test
    internal fun shouldConvertFromDtoToTransaction(@Random transactionDto: TransactionDTO) {
        val transaction = TransactionConverter.from(transactionDto)

        assertThat(transaction.id, `is`(transactionDto.id))
        assertThat(transaction.remitterAccount.id, `is`(transactionDto.remitterAccountId))
        assertThat(transaction.beneficiaryAccount.id, `is`(transactionDto.beneficiaryAccountId))
        assertThat(transaction.currencyCode, `is`(transactionDto.amount.currencyCode))
        assertThat(transaction.amount, `is`(transactionDto.amount.value))
    }
}