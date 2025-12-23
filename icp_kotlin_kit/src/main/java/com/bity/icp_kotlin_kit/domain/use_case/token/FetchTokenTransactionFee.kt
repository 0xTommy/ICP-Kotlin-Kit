package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger
import java.math.BigInteger

class FetchTokenTransactionFee internal constructor(
    private val tokenRepositoryFactory: TokenRepositoryFactory
) {

    suspend operator fun invoke(token: ICPToken): Result<BigInteger> {
        val repository = tokenRepositoryFactory.createRepository(
            standard = token.standard,
            canister = token.canister
        )
        return try {
            val fee = repository.fee()
            Result.success(fee)
        } catch (t: Throwable) {
            ICPKitLogger.logError(
                "Failed to fetch transaction fee for token ${token.canister}",
                t
            )
            Result.failure(t)
        }
    }

}