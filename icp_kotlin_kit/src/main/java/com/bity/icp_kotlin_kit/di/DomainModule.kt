package com.bity.icp_kotlin_kit.di

import com.bity.icp_kotlin_kit.data.factory.TokenRepositoryFactoryImpl
import com.bity.icp_kotlin_kit.data.factory.TransactionRepositoryFactoryImpl
import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.domain.use_case.QueryBlocks
import com.bity.icp_kotlin_kit.domain.use_case.nft.FetchAllNFTCollections
import com.bity.icp_kotlin_kit.domain.use_case.nft.FetchNFTCollection
import com.bity.icp_kotlin_kit.domain.use_case.nft.FetchNFTCollectionTokenOwner
import com.bity.icp_kotlin_kit.domain.use_case.nft.FetchNFTCollectionTokens
import com.bity.icp_kotlin_kit.domain.use_case.nft.FetchUserNFTTokensHolding
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchAllTokens
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchTokenBalance
import com.bity.icp_kotlin_kit.domain.use_case.transaction.FetchTokenTransactionFee
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchTokensBalance
import com.bity.icp_kotlin_kit.domain.use_case.transaction.SendToken
import com.bity.icp_kotlin_kit.domain.use_case.transaction.FetchAccountTransactions
import com.bity.icp_kotlin_kit.domain.use_case.transaction.FetchTokenTransactions
import com.bity.icp_kotlin_kit.domain.use_case.transaction.GetTransactionExplorerUrl
import com.bity.icp_kotlin_kit.domain.use_case.transaction.SendICP

object DomainModule {

    internal val tokenRepositoryFactory: TokenRepositoryFactory by lazy {
        TokenRepositoryFactoryImpl()
    }

    internal val transactionRepositoryFactory: TransactionRepositoryFactory by lazy {
        TransactionRepositoryFactoryImpl(
            snsService = DataModule.snsCachedRepository,
            indexService = DataModule.icpIndexService
        )
    }

    fun provideFetchTokensBalance(): FetchTokensBalance =
        FetchTokensBalance(
            fetchAllTokens = provideFetchAllTokens(),
            fetchTokenBalance = provideFetchTokenBalance()
        )

    fun provideFetchAllTokens(): FetchAllTokens =
        FetchAllTokens(
            tokenCache = RepositoryModule.tokenCache,
            tokenRepository = RepositoryModule.tokenRepository
        )

    fun provideFetchTokenBalance(): FetchTokenBalance =
        FetchTokenBalance(
            tokenRepositoryFactory = tokenRepositoryFactory
        )

    fun provideFetchAccountTransactions() : FetchAccountTransactions =
        FetchAccountTransactions(
            fetchAllTokens = provideFetchAllTokens(),
            fetchTokenTransactions = provideFetchTokenTransactions()
        )

    fun provideFetchTokenTransactions() : FetchTokenTransactions =
        FetchTokenTransactions(
            transactionRepositoryFactory = transactionRepositoryFactory
        )

    fun provideFetchTokenTransactionFee() : FetchTokenTransactionFee =
        FetchTokenTransactionFee(
            tokenRepositoryFactory = tokenRepositoryFactory
        )

    fun provideSendToken() : SendToken =
        SendToken(
            tokenRepositoryFactory = tokenRepositoryFactory
        )

    fun provideSendICP() : SendICP =
        SendICP(
            repository = RepositoryModule.ledgerCanisterRepository
        )

    fun provideQueryBlocks() : QueryBlocks =
        QueryBlocks(
            repository = RepositoryModule.ledgerCanisterRepository
        )

}

/**
 * NFT Use Cases
 */
val fetchAllNFTCollections by lazy {
    FetchAllNFTCollections(
        nftCachedRepository = nftCachedRepository
    )
}

val fetchNFTCollection by lazy {
    FetchNFTCollection(
        nftCachedRepository = nftCachedRepository
    )
}

val fetchNFTCollectionTokens by lazy {
    FetchNFTCollectionTokens(
        nftCachedRepository = nftCachedRepository
    )
}

val fetchNFTCollectionTokenOwner by lazy {
    FetchNFTCollectionTokenOwner(
        nftCachedRepository = nftCachedRepository
    )
}

val fetchUserNFTTokensHolding by lazy {
    FetchUserNFTTokensHolding(
        nftCachedRepository = nftCachedRepository
    )
}