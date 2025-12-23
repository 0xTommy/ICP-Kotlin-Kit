package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.data.model.error.RemoteClientError
import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger
import java.math.BigInteger

class FetchTokenBalance internal constructor(
    private val tokenRepositoryFactory: TokenRepositoryFactory
) {
    suspend operator fun invoke(
        token: ICPToken,
        principal: ICPPrincipal
    ): Result<BigInteger> {
        val repository = try {
            tokenRepositoryFactory.createRepository(
                standard = token.standard,
                canister = token.canister
            )
        } catch (ex: ICPKitException) {
            ICPKitLogger.logError(
                message = "Failed to create repository for token $token",
                throwable = ex
            )
            return Result.failure(ex)
        }
        return try {
            val balance = repository.fetchBalance(principal)
            Result.success(balance)
        } catch (ex: RemoteClientError) {
            ICPKitLogger.logError(
                message = "Failed to fetch balance for token $token",
                throwable = ex
            )
            Result.failure(ex)
        }
    }
}