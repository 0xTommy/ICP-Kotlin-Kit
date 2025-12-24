package com.bity.icp_kotlin_kit.domain.use_case.explorer_url

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchAllTokens

class GetTransactionExplorerUrl internal constructor(
    private val fetchAllTokens: FetchAllTokens,
    private val transactionRepositoryFactory: TransactionRepositoryFactory
) {

    suspend operator fun invoke(
        tokenCanister: ICPPrincipal,
        transactionIndex: String
    ): Result<String> {
        val token = fetchAllTokens()
            .getOrNull()
            ?.firstOrNull { it.canister.string == tokenCanister.string }
            ?: return Result.failure(ICPKitException.TokenNotFound(tokenCanister))
        val service = transactionRepositoryFactory.getExplorerURLRepository(token)
            ?: return Result.failure(ICPKitException.TokenNotSupported(token))
        return try {
            val url = service.getExplorerURL(transactionIndex)
            Result.success(url)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}