package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.repository.ICPTransactionRepository
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository

class GetTransactionExplorerUrl internal constructor(
    private val tokenRepository: TokenRepository,
    private val transactionRepository: ICPTransactionRepository
) {

    suspend operator fun invoke(
        tokenCanister: ICPPrincipal,
        transactionIndex: String
    ): String {
        TODO()
        /*val token = tokenRepository.fetchAllTokens()
            .firstOrNull { it.canister.string == tokenCanister.string }
            ?: throw ICPKitException.TokenNotFound(tokenCanister)
        return transactionRepository.getTransactionExplorerURL(
            token = token,
            transactionId = transactionIndex
        )*/
    }

}