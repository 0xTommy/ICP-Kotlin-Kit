package com.bity.icp_kotlin_kit.data.factory

import com.bity.icp_kotlin_kit.data.service.nft.EXTNFTService
import com.bity.icp_kotlin_kit.domain.generated_file.DBANFTService
import com.bity.icp_kotlin_kit.data.service.nft.ICRC7NFTService
import com.bity.icp_kotlin_kit.data.service.nft.OrigynNFTService
import com.bity.icp_kotlin_kit.domain.factory.NFTServiceFactory
import com.bity.icp_kotlin_kit.domain.generated_file.EXTService
import com.bity.icp_kotlin_kit.domain.generated_file.OrigynNFT
import com.bity.icp_kotlin_kit.domain.model.ICPNftCollection
import com.bity.icp_kotlin_kit.domain.model.enum.ICPNftStandard
import com.bity.icp_kotlin_kit.domain.service.NFTService

internal class NFTServiceFactoryImpl: NFTServiceFactory {

    override fun createNFTService(collection: ICPNftCollection): NFTService? {
        return when(collection.standard) {

            ICPNftStandard.EXT ->
                EXTNFTService(
                    canister = collection.canister,
                    service = EXTService(
                        canister = collection.canister
                    )
                )

            ICPNftStandard.ICRC7 ->
                ICRC7NFTService(
                    canister = collection.canister,
                    service = DBANFTService(
                        canister = collection.canister,
                    ),
                )

            ICPNftStandard.ORIGYN_NFT ->
                OrigynNFTService(
                    canister = OrigynNFT.Nft_Canister(
                        canister = collection.canister
                    )
                )

            else -> null
        }
    }

}