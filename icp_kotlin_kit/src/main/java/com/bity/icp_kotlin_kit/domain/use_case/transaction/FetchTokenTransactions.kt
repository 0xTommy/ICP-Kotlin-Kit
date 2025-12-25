package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger

class FetchTokenTransactions internal constructor(
    private val transactionRepositoryFactory: TransactionRepositoryFactory
) {

    suspend operator fun invoke(
        account: ICPAccount,
        token: ICPToken
    ): Result<List<ICPTokenTransaction>> {
        val repository = transactionRepositoryFactory.getTransactionRepository(token)
            ?: return Result.failure(ICPKitException.TokenNotSupported(token))
        return try {
            val transactions = repository.fetchAllTransactions(account)
            Result.success(transactions)
        } catch (t: Throwable) {
            ICPKitLogger.logError(
                "Failed to fetch transactions for account $account and token $token",
                t
            )
            Result.failure(t)
        }
    }

}