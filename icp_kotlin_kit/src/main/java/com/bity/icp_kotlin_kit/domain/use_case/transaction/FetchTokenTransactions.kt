package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction

class FetchTokenTransactions internal constructor(
    private val transactionRepositoryFactory: TransactionRepositoryFactory
) {

    suspend operator fun invoke(
        account: ICPAccount,
        token: ICPToken
    ): List<ICPTokenTransaction> {
        val repository = transactionRepositoryFactory.getTransactionRepository(token)
            ?: throw ICPKitException.TokenNotSupported(token)
        return repository.fetchAllTransactions(account)
    }

}