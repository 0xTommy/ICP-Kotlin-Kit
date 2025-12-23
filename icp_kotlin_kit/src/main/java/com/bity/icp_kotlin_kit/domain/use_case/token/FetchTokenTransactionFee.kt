package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import java.math.BigInteger

class FetchTokenTransactionFee internal constructor(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(token: ICPToken): BigInteger =
        tokenRepository.fee(token)

}