package com.bity.icp_kotlin_kit.domain.exception

import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.enum.ICPNftStandard
import com.bity.icp_kotlin_kit.domain.model.enum.ICPTokenStandard
import java.math.BigInteger

sealed class ICPKitException(message: String? = null) : Exception(message) {

    class TokenNotFound(
        tokenCanister: ICPPrincipal
    ): ICPKitException("Token ${tokenCanister.string} not found")

    class NFTNotFound(
        collectionPrincipal: ICPPrincipal,
        nftId: BigInteger
    ): ICPKitException("NFT $nftId not found in collection $collectionPrincipal")

    class InvalidNFTToken(
        canister: ICPPrincipal,
        token: String
    ): ICPKitException("Invalid NFT token $token for canister ${canister.string }")

    class NFTCollectionNotFound(
        collectionPrincipal: ICPPrincipal
    ): ICPKitException("NFT Collection not found: $collectionPrincipal")

    class NFTStandardNotSupported(
        standard: ICPNftStandard
    ) : ICPKitException("Standard not supported: $standard")

    class TokenNotSupported(
        token: ICPToken
    ) : ICPKitException("Token not supported: ${token.symbol}, ${token.canister.string}")

    class RepositoryNotAvailable(
        standard: ICPTokenStandard,
        canister: ICPPrincipal
    ): ICPKitException("Unable to create repository for token with standard $standard and canister $canister")

    class InvalidBlocksLength(
        expected: Int,
        returned: Int
    ) : ICPKitException("Expected $expected blocks, but returned $returned")

}