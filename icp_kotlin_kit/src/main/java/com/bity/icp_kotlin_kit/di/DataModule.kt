package com.bity.icp_kotlin_kit.di

import com.bity.icp_kotlin_kit.data.datasource.api.service.ICPRetrofitService
import com.bity.icp_kotlin_kit.data.factory.NFTRepositoryFactoryImpl
import com.bity.icp_kotlin_kit.data.factory.TokenRepositoryFactoryImpl
import com.bity.icp_kotlin_kit.data.factory.TransactionRepositoryFactoryImpl
import com.bity.icp_kotlin_kit.data.repository.ICPCanisterRepositoryImpl
import com.bity.icp_kotlin_kit.data.repository.LedgerCanisterRepositoryImpl
import com.bity.icp_kotlin_kit.data.repository.NFTCachedRepositoryImpl
import com.bity.icp_kotlin_kit.data.repository.SNSCachedRepositoryImpl
import com.bity.icp_kotlin_kit.data.repository.TokenRepositoryImpl
import com.bity.icp_kotlin_kit.data.service.nft.NFTCollectionIdServiceImpl
import com.bity.icp_kotlin_kit.domain.factory.NFTRepositoryFactory
import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.factory.TransactionRepositoryFactory
import com.bity.icp_kotlin_kit.data.generated_file.DABNFT
import com.bity.icp_kotlin_kit.data.generated_file.ICRC1Oracle
import com.bity.icp_kotlin_kit.data.generated_file.LedgerCanister
import com.bity.icp_kotlin_kit.data.generated_file.NNSICPIndexCanister
import com.bity.icp_kotlin_kit.data.generated_file.NNS_SNS_W
import com.bity.icp_kotlin_kit.domain.model.enum.ICPSystemCanisters
import com.bity.icp_kotlin_kit.domain.repository.ICPCanisterRepository
import com.bity.icp_kotlin_kit.domain.repository.LedgerCanisterRepository
import com.bity.icp_kotlin_kit.domain.repository.NFTCachedRepository
import com.bity.icp_kotlin_kit.domain.repository.SNSCachedRepository
import com.bity.icp_kotlin_kit.domain.service.NFTCollectionIdService
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.util.jackson.CborConverterFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal object DataModule {

    val icrc1OracleCanister by lazy {
        ICRC1Oracle.ICRC1OracleCanister(
            canister = ICPSystemCanisters.ICRC1Oracle.icpPrincipal
        )
    }

    val snsCachedRepository: SNSCachedRepository by lazy {
        SNSCachedRepositoryImpl(
            canister = nnsSNSWService
        )
    }

    val icpIndexService: NNSICPIndexCanister.NNSICPIndexCanisterService by lazy {
        NNSICPIndexCanister.NNSICPIndexCanisterService(
            canister = ICPSystemCanisters.Index.icpPrincipal
        )
    }

    val ledgerCanisterService: LedgerCanister.LedgerCanisterService by lazy {
        LedgerCanister.LedgerCanisterService(
            canister = ICPSystemCanisters.Ledger.icpPrincipal
        )
    }

}

private const val BASE_URL: String = "https://icp-api.io/api/v2/canister/"
private val objectMapper = ObjectMapper(CBORFactory())
private val cborConverterFactory = CborConverterFactory.create(
    objectMapper.apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
)
private val httpClient = OkHttpClient()
    .newBuilder()
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

private val icpRetrofitService: ICPRetrofitService =
    Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(cborConverterFactory)
        .build()
        .create(ICPRetrofitService::class.java)

internal val icpCanisterRepository: ICPCanisterRepository by lazy {
    ICPCanisterRepositoryImpl(
        icpRetrofitService = icpRetrofitService,
    )
}

/**
 * Service
 */
val nftCollectionIdService: NFTCollectionIdService by lazy {
    NFTCollectionIdServiceImpl()
}

/**
* Canister from generated file
 */

internal val nnsSNSWService: NNS_SNS_W.nns_sns_wService by lazy {
    NNS_SNS_W.nns_sns_wService(
        canister = ICPSystemCanisters.NNS_SNS_W.icpPrincipal
    )
}

private val dabNFTService: DABNFT.DABNFTService by lazy {
    DABNFT.DABNFTService(
        canister = ICPSystemCanisters.NFTRegistry.icpPrincipal
    )
}

/**
 * Factory
 */



internal val nftRepositoryFactory: NFTRepositoryFactory by lazy {
    NFTRepositoryFactoryImpl()
}

/**
 * Repository
 */
internal val nftCachedRepository: NFTCachedRepository by lazy {
    NFTCachedRepositoryImpl(
        dabCanister = dabNFTService,
        nftRepositoryFactory = nftRepositoryFactory
    )
}