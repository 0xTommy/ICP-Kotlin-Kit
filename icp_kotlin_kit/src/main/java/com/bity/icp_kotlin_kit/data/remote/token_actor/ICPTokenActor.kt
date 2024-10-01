package com.bity.icp_kotlin_kit.data.remote.token_actor

import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.ICPTokenMetadata
import com.bity.icp_kotlin_kit.domain.model.ICPTokenTransfer
import com.bity.icp_kotlin_kit.domain.model.arg.ICPTokenTransferArgs
import java.math.BigInteger

internal interface ICPTokenActor {
    suspend fun getBalance(principal: ICPPrincipal): BigInteger
    suspend fun metadata(): ICPTokenMetadata
    suspend fun fee(): BigInteger
    suspend fun transfer(args: ICPTokenTransferArgs): ICPTokenTransfer
}