package com.bity.icp_kotlin_kit.di

import com.bity.icp_kotlin_kit.data.repository.InMemoryTokenCache
import com.bity.icp_kotlin_kit.data.repository.LedgerCanisterRepositoryImpl
import com.bity.icp_kotlin_kit.data.repository.TokenRepositoryImpl
import com.bity.icp_kotlin_kit.domain.repository.LedgerCanisterRepository
import com.bity.icp_kotlin_kit.domain.repository.TokenCache
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository

internal object RepositoryModule {

    val tokenCache : TokenCache by lazy {
        InMemoryTokenCache()
    }

    val tokenRepository : TokenRepository by lazy {
        TokenRepositoryImpl(
            canister = DataModule.icrc1OracleCanister
        )
    }

    val ledgerCanisterRepository: LedgerCanisterRepository by lazy {
        LedgerCanisterRepositoryImpl(
            ledgerCanisterService = DataModule.ledgerCanisterService
        )
    }

}