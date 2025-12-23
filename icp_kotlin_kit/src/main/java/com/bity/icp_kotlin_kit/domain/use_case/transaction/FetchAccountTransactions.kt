package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.model.ICPAccount
import com.bity.icp_kotlin_kit.domain.model.token_transaction.ICPTokenTransaction
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchAllTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FetchAccountTransactions internal constructor(
    private val fetchAllTokens: FetchAllTokens,
    private val fetchTokenTransactions: FetchTokenTransactions
) {

    operator fun invoke(account: ICPAccount): Flow<List<ICPTokenTransaction>> = flow {

        val tokens = fetchAllTokens().getOrElse {
            throw it
        }

        coroutineScope {
            tokens.map { token ->
                async {
                    fetchTokenTransactions(
                        account = account,
                        token = token
                    ).getOrNull()
                }
            }.forEach { deferred ->
                deferred.await()?.let { emit(it) }
            }
        }
    }.flowOn(Dispatchers.IO)

}