package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import java.math.BigInteger

class FetchTokenTransactionFee internal constructor(
    private val tokenRepositoryFactory: TokenRepositoryFactory
) {

    suspend operator fun invoke(token: ICPToken): BigInteger {
        val repository = tokenRepositoryFactory.createRepository(
            standard = token.standard,
            canister = token.canister
        )
        return repository.fee()
    }

}