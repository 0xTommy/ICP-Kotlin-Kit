package com.bity.icp_kotlin_kit.data.repository

import com.bity.icp_kotlin_kit.data.generated_file.*
import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.ICPTokenTransfer
import com.bity.icp_kotlin_kit.domain.model.arg.ICPTokenTransferArgs
import com.bity.icp_kotlin_kit.domain.model.enum.ICPTokenStandard
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger

internal class TokenRepositoryImpl(
    private val canister: ICRC1Oracle.ICRC1OracleCanister,
): TokenRepository {

    override suspend fun getTokenCount(): ULong =
        canister.count_icrc1_canisters()

    override suspend fun fetchTokensPage(
        startAt: ULong,
        pageSize: ULong
    ): List<ICPToken> {
        return canister.get_icrc1_paginated(
            startAt = startAt,
            pageSize = pageSize
        )
            .map { it.toDomainModel() }
    }

    override suspend fun send(transferArgs: ICPTokenTransferArgs): ICPTokenTransfer {
        TODO()
        /*val repository = tokenRepositoryFactory.createRepository(
            standard = transferArgs.token.standard,
            canister = transferArgs.token.canister
        )
        return repository.transfer(transferArgs)*/
    }

}

private fun ICRC1Oracle.ICRC1.toDomainModel(): ICPToken =
    ICPToken(
        standard = category.toDomainModel(),
        canister = ICPPrincipal(ledger),
        name = name,
        decimals = decimals.toInt(),
        symbol = symbol,
        spam = category.isSpam(),
        logo = logo,
    )

private fun ICRC1Oracle.Category.toDomainModel(): ICPTokenStandard =
    when(this) {
        ICRC1Oracle.Category.Native -> ICPTokenStandard.ICP
        else -> ICPTokenStandard.ICRC1
    }

private fun ICRC1Oracle.Category.isSpam(): Boolean =
    when(this) {
        ICRC1Oracle.Category.Spam -> true
        else -> false
    }