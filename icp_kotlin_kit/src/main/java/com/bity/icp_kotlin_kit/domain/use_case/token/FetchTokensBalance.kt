package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPTokenBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigInteger

class FetchTokensBalance internal constructor(
    private val fetchAllTokens: FetchAllTokens,
    private val fetchTokenBalance: FetchTokenBalance
) {

    /**
     * Fetches token balances as they become available.
     * Emits each balance as soon as it's fetched, allowing for progressive updates.
     *
     * @param principal The principal to fetch balances for
     * @return Flow of token balances (only non-zero balances)
     */
    operator fun invoke(principal: ICPPrincipal): Flow<ICPTokenBalance> = flow {

        val result = fetchAllTokens()
        val tokens = result.getOrElse {
            throw it
        }

        coroutineScope {
            tokens.map { token ->
                async {
                    val balance = fetchTokenBalance(
                        token = token,
                        principal = principal
                    ).getOrNull()

                    if (balance != null && balance != BigInteger.ZERO) {
                        ICPTokenBalance(token = token, balance = balance)
                    } else {
                        null
                    }
                }
            }.forEach { deferred ->
                deferred.await()?.let { tokenBalance ->
                    emit(tokenBalance)
                }
            }
        }
    }.flowOn(Dispatchers.IO)

}