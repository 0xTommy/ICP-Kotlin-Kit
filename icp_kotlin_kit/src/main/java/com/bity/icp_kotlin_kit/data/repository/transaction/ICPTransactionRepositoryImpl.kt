package com.bity.icp_kotlin_kit.data.repository.transaction

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction
import com.bity.icp_kotlin_kit.domain.repository.ICPTransactionRepository

internal class ICPTransactionRepositoryImpl(
    private val transactionRepositoryFactory: TransactionRepositoryFactory
) : ICPTransactionRepository {

    override suspend fun getTransactionExplorerURL(
        token: ICPToken,
        transactionId: String,
    ): String {
        val service = transactionRepositoryFactory.getExplorerURLRepository(token)
            ?: throw ICPKitException.TokenNotSupported(token)
        return service.getExplorerURL(transactionId)
    }
}