package com.bity.icp_kotlin_kit.data.factory

import com.bity.icp_kotlin_kit.data.datasource.api.model.toDomainModel
import com.bity.icp_kotlin_kit.data.remote.transaction_provider.ICPICRC1IndexTransactionProvider
import com.bity.icp_kotlin_kit.data.remote.transaction_provider.ICPIndexTransactionProvider
import com.bity.icp_kotlin_kit.data.remote.url_provider.ICPExplorerURLProvider
import com.bity.icp_kotlin_kit.data.remote.url_provider.ICPTokenExplorerURLProvider
import com.bity.icp_kotlin_kit.domain.factory.TransactionProviderFactory
import com.bity.icp_kotlin_kit.domain.generated_file.NNSICPIndexCanister
import com.bity.icp_kotlin_kit.domain.provider.ICPTransactionProvider
import com.bity.icp_kotlin_kit.domain.generated_file.NNS_SNS_W
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.enum.ICPSystemCanisters
import com.bity.icp_kotlin_kit.domain.provider.ExplorerURLProvider
import com.bity.icp_kotlin_kit.domain.repository.SNSCachedService

internal class TransactionProviderFactoryImpl(
    private val snsService: SNSCachedService,
    private val indexService: NNSICPIndexCanister.NNSICPIndexCanisterService
): TransactionProviderFactory {

    override suspend fun getTransactionProvider(token: ICPToken): ICPTransactionProvider? {
        // TODO: Support DIP20 tokens
        if(token.canister == ICPSystemCanisters.Ledger.icpPrincipal)
            return ICPIndexTransactionProvider(
                icpToken = token,
                indexService = indexService
            )
        val index = findSNS(token.canister)?.index_canister_id
            ?.toDomainModel()
            ?: return null
        return ICPICRC1IndexTransactionProvider(
            icpToken = token,
            indexCanister = index
        )
    }

    override suspend fun getExplorerURLProvider(token: ICPToken): ExplorerURLProvider? {
        if(token.canister == ICPSystemCanisters.Ledger.icpPrincipal)
            return ICPExplorerURLProvider()
        val rootCanisterId = findSNS(token.canister)?.root_canister_id
            ?: return null
        return ICPTokenExplorerURLProvider(rootCanisterId.toDomainModel())
    }

    private suspend fun findSNS(tokenCanister: ICPPrincipal): NNS_SNS_W.DeployedSns? {
        return snsService.deployedSNSes().firstOrNull {
            it.root_canister_id?.toDomainModel() == tokenCanister
                    || it.governance_canister_id?.toDomainModel() == tokenCanister
                    || it.index_canister_id?.toDomainModel() == tokenCanister
                    || it.swap_canister_id?.toDomainModel() == tokenCanister
                    || it.ledger_canister_id?.toDomainModel() == tokenCanister
        }
    }
}